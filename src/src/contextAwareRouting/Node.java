package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Node {
	private int nodeID;

	private double xpos;
	private double ypos;
	private int serviceTime;
	private boolean waiting;
	private int nextNodeID;
	private boolean sentToServer;

	private Request reqInService;

	private ArrayList<Integer> appList;

	private  LinkedList<Request> queue;

	public Node (int nodeID, double xpos, double ypos) {
		this.nodeID = nodeID;

		this.xpos = xpos;
		this.ypos = ypos;
		this.queue = new LinkedList<Request>();

		this.setServiceTime(0);
		this.setWaiting(false);
		setSentToServer(false);
	}

	public Node (int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		this(nodeID,xpos,ypos);
		this.appList = appList;
	}

	public void run() {
		if (!isWaiting()) {
			if (!processingRequest()) {
				if (getReqInService() != null) {
					Request req = getReqInService();
					handleRequestAfterProcessing(req);
				} else {
					serviceNextRequest();
					if (getServiceTime() == 0 && getReqInService() != null) {
						sendToServer();
					}
				}
			}
		}
	}

	protected void deployRequest() {
		Request request = getReqInService();
		int nextNode = getNextNodeID();
		if (nextNode != 9999999)
			request.setCurrentNodeID(nextNode);
		Mainline.server.retrieveNode(nextNode).addRequest(request);
	}

	protected void sendToServer() {
		Mainline.server.addNodeRequest(this.getNodeID());
		setWaiting(true);
		setSentToServer(true);
	}

	private boolean processingRequest() {
		setServiceTime(getServiceTime() - 1);
		return (getServiceTime() > 0);
	}

	protected void calculateServiceTime(Request request) {
		RandomNumGen generator = new RandomNumGen();

		int reqApp = request.getApp();
		int minDist = Mainline.numApps;
		for (Integer app:appList) {
			int dist = Math.max(0, Math.min(Math.abs(reqApp - app), Math.min(reqApp, app) + Mainline.numApps - Math.max(reqApp, app)));
			if (dist < minDist)
				minDist = dist;
		}
		double rate = (minDist == 0) ? 2.0 : 1/(0.5 * Math.log((double) minDist) + 1.0);

		setServiceTime( (int) (generator.nextExp(rate) * 100)); // service time in milliseconds
	}

	//NODE ID
	public int getNodeID() {
		return this.nodeID;
	}

	//Position Methods
	public double getXpos() {
		return this.xpos;
	}

	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	public double getYpos() {
		return this.ypos;
	}

	public void setYpos(double ypos) {
		this.ypos = ypos;
	}

	//Applist Methods
	public ArrayList<Integer> getAppList(){
		return appList;
	} 	

	public void addApp(Integer app){

		appList.add(app);
	} 	

	public void remove(Integer app){
		if (appList.contains(app))
			appList.remove(appList.indexOf(app));
	}

	//Queue Methods
	public Request getNextRequest() {
		return queue.getFirst();
	}

	public void addRequest (Request request) {
		request.setInQueue(true);
		queue.add(request);
	}

	protected Request removeRequest() {
		return queue.remove();
	}

	public int getQueueSize() {
		return queue.size();
	}

	public LinkedList<Request> getQueue(){
		return queue;
	}

	public Request getReqInService() {
		return reqInService;
	}

	public void setReqInService (Request request) {
		this.reqInService = request;
	}

	//Service Time Methods
	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		if (serviceTime < 0)
			this.serviceTime = 0;
		else 
			this.serviceTime = serviceTime;

	}

	//Waiting For Server flag
	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	//Node ID of next node in path
	public int getNextNodeID() {
		return nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}
	
	//Sent to Server flag
	public boolean isSentToServer() {
		return sentToServer;
	}

	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}

	protected abstract void serviceNextRequest();
	protected abstract void handleRequestAfterProcessing(Request request);


}
