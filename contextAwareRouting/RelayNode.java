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
	protected void handleRequest() {
		if (getQueueSize() != 0) {
			Request nextReq = getNextRequest();
			nextReq.setInQueue(false);

			setHandlingRequest(true);
			sendToServer();
		}
	}
}
