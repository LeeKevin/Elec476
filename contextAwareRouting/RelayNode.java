package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode extends Node{

	public RelayNode(int nodeID, int xpos, int ypos) {
		super(nodeID, xpos, ypos);
	}

	public RelayNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue) {
		super(nodeID, xpos, ypos, queue);
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
