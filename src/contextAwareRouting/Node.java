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
	private boolean handlingRequest;
	
	private ArrayList<Integer> appList;
	
	private  LinkedList<Request> queue;

	public Node (int nodeID, double xpos, double ypos) {
		this.nodeID = nodeID;

		this.xpos = xpos;
		this.ypos = ypos;
		this.queue = new LinkedList<Request>();

		this.setServiceTime(0);
		this.setWaiting(false);
		this.setHandlingRequest(false);
	}

	public Node (int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		this(nodeID,xpos,ypos);
		this.appList = appList;
	}
	
	

	public void run() {
		if (!isWaiting()) {
			if (isHandlingRequest()) {
				deployRequest();
				setHandlingRequest(false);
			} else {
				handleRequest();
			}
		}		
	}

	protected abstract void handleRequest();

	protected void sendToServer() {
		Mainline.server.addNodeRequest(this.getNodeID());
		setWaiting(true);
	}
	
	protected void processRequest(Request nextReq) {
		RandomNumGen generator = new RandomNumGen();

		int reqApp = nextReq.getApp();
		int minDist = Mainline.numApps;
		for (Integer app:appList) {
			int dist = Math.max(0, Math.min(Math.abs(reqApp - app), Math.min(reqApp, app) + Mainline.numApps - Math.max(reqApp, app)));
			if (dist < minDist)
				minDist = dist;
		}
		double rate = (minDist == 0) ? 2.0 : 1/(0.5 * Math.log((double) minDist) + 1.0);

		setServiceTime( (int) (generator.nextExp(rate) * 100)); // service time in milliseconds

		nextReq.setInProcess(true);
	}
	
	public int getNodeID() {
		return this.nodeID;
	}

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

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public int getNextNodeID() {
		return nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}

	public boolean isHandlingRequest() {
		return handlingRequest;
	}

	public void setHandlingRequest(boolean handlingRequest) {
		this.handlingRequest = handlingRequest;
	}

	protected void deployRequest() {
		Request request = removeRequest();
		if (nextNodeID != 9999999) {
			request.setCurrentNodeID(nextNodeID);
		}
		Mainline.server.retrieveNode(nextNodeID).addRequest(request);
	}
	
	public LinkedList<Request> getQueue(){
		return queue;
	}

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

}
