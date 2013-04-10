package contextAwareRouting;

import java.util.ArrayList;

public class UserNode extends Node{

	public UserNode(int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos, appList);
	}

	@Override
	protected void serviceNextRequest() {
		if (getQueueSize() != 0) {
			//Pull next request from queue
			Request nextReq = removeRequest();
			setReqInService(nextReq);
			nextReq.setInQueue(false);
			setSentToServer(false);

			if (nextReq.getSourceNodeID() == this.getNodeID()) {
				//If request is leaving or returning to source node, no service time
				setServiceTime(0);
				if (nextReq.getStatus().equals(Request.Status.INCOMING)) {
					// Retire request, if incoming
					nextReq.calculateTimeInSystem(Mainline.time);
					setReqInService(null);
				} 
			}
			else if(nextReq.getDestinationNodeID() == this.getNodeID() && nextReq.getStatus().equals(Request.Status.OUTGOING))
				calculateServiceTime(nextReq);
		}		
	}

	@Override
	protected void handleRequestAfterProcessing(Request request) {
		if (isSentToServer())
			deployRequest();
		else
			sendToServer();

	}

	public void updateLocation(double XrandPos, double YrandPos){
		setXpos(this.getXpos() + XrandPos);
		setYpos(this.getYpos() + YrandPos);
	}

}
