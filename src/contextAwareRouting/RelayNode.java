package contextAwareRouting;

import java.util.ArrayList;

public class RelayNode extends Node{

	public RelayNode(int nodeID, double xpos, double ypos) {
		super(nodeID, xpos, ypos);
	}

	public RelayNode(int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos, appList);
	}

	@Override
	protected void serviceNextRequest() {
		//Pull next request from queue, set its status and calc its service time
		reqInService = queue.remove();
		reqInService.setInQueue(false);
		calculateServiceTime(reqInService);
		sendToServer();		
	}
}
