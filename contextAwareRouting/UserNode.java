package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class UserNode extends Node{

	private ArrayList<Integer> appList;


	public UserNode(int nodeID, int xpos, int ypos) {
		super(nodeID, xpos, ypos);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue) {
		super(nodeID, xpos, ypos, queue);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, int xpos, int ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos);
		this.appList = appList;		
	}
	public UserNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos);
		this.appList = new ArrayList<Integer>();		
	}

	public void run() {
		if (!isWaiting()) {
			if (isHandlingRequest()) {
				deployRequest();
				setHandlingRequest(false);
			} else {
				int serviceTime = getServiceTime();
				if (serviceTime > 0)
					setServiceTime(serviceTime - 1);
				else {
					setServiceTime(0);
					handleNextRequest();
					setHandlingRequest(true);
				}
			}
		}
	}

	private void handleNextRequest() {
		Request nextReq = getNextRequest();
		if (nextReq.getSourceNodeID() == this.getNodeID()) {
			Mainline.server.addNode(this.getNodeID());
			setWaiting(true);
		}
		else if(nextReq.getDestinationNodeID() == this.getNodeID()) {
			setServiceTime(processRequest(nextReq));
		}
	}

	private int processRequest(Request nextReq) {
		RandomNumGen generator = new RandomNumGen();

		int reqApp = nextReq.getApp();
		int minDist = Mainline.numApps;
		for (Integer app:appList) {
			int dist = Math.max(0, Math.min(Math.abs(reqApp - app), Math.min(reqApp, app) + Mainline.numApps - Math.max(reqApp, app)));
			if (dist < minDist)
				minDist = dist;
		}
		double rate = (minDist == 0) ? 2.0 : 1/(Math.log((double) minDist) + 1.0);

		return (int) (generator.nextExp(rate) * 100); // service time in milliseconds
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
