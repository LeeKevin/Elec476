package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class UserNode extends Node{

	private ArrayList<Integer> appList;


	public UserNode(int nodeID, double xpos, double ypos) {
		super(nodeID, xpos, ypos);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, double xpos, double ypos, LinkedList<Request> queue) {
		super(nodeID, xpos, ypos, queue);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos);
		this.appList = appList;		
	}
	public UserNode(int nodeID, double xpos, double ypos, LinkedList<Request> queue, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos, queue);
		this.appList = new ArrayList<Integer>();		
	}

	@Override
	protected void handleRequest() {
		int serviceTime = getServiceTime();
		if (serviceTime > 0)
			setServiceTime(serviceTime - 1);
		else {
			setServiceTime(0);
			
			if (getQueueSize() != 0) {
				Request nextReq = getNextRequest();
				if (nextReq.isInProcess()) {
					nextReq.returnRequestToSource();
					setHandlingRequest(true);
					nextReq.setInProcess(false);
					sendToServer();
				}
				nextReq.setInQueue(false);
				if (nextReq.getSourceNodeID() == this.getNodeID()) {
					if (nextReq.getStatus().equals(Request.Status.OUTGOING)) {
						setHandlingRequest(true);
						sendToServer();
					} else {
						// Data is retrieved from destination node
						// Retire request
						nextReq.calculateTimeInSystem(Mainline.time);
						removeRequest();
					}
				}
				else if(nextReq.getDestinationNodeID() == this.getNodeID() && nextReq.getStatus().equals(Request.Status.OUTGOING))
					processRequest(nextReq);			
			}
		}		
	} 	

	private void processRequest(Request nextReq) {
		RandomNumGen generator = new RandomNumGen();

		int reqApp = nextReq.getApp();
		int minDist = Mainline.numApps;
		for (Integer app:appList) {
			int dist = Math.max(0, Math.min(Math.abs(reqApp - app), Math.min(reqApp, app) + Mainline.numApps - Math.max(reqApp, app)));
			if (dist < minDist)
				minDist = dist;
		}
		double rate = (minDist == 0) ? 2.0 : 1/(Math.log((double) minDist) + 1.0);

		setServiceTime( (int) (generator.nextExp(rate) * 100)); // service time in milliseconds

		nextReq.setInProcess(true);
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
