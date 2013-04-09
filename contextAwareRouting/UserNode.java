package contextAwareRouting;

import java.util.ArrayList;

public class UserNode extends Node{

	public UserNode(int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos, appList);
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

}
