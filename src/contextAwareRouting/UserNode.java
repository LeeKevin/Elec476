package contextAwareRouting;

import java.util.ArrayList;

import contextAwareRouting.Request.Status;

public class UserNode extends Node{

	public UserNode(int nodeID, double xpos, double ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos, appList);
	}

	@Override
	protected void serviceNextRequest() {
		//Pull next request from queue
		reqInService = queue.remove();
		reqInService.setInQueue(false);
		
		//start/end condition
		if (reqInService.getSourceNodeID() == nodeID) {
			serviceTime = 0;
			//If request has returned
			if (reqInService.getStatus().equals(Request.Status.INCOMING)) {
				// Retire request, if incoming
				reqInService.calculateTimeInSystem(Mainline.time);
				reqInService.setStatus(Status.ARRIVED);
				setReqInService(null);
				Mainline.numdone++;
				System.out.println((double)Mainline.numdone/Mainline.reqCount);
				//else outgoing so ask server for next node
			} else {
				sendToServer();
			}
		}
		
		//else has to be middle condition
		else if(reqInService.getDestinationNodeID() == this.getNodeID() && reqInService.getStatus().equals(Request.Status.OUTGOING)){
			reqInService.returnRequestToSource();
			calculateServiceTime(reqInService);
			sendToServer();
		}
		
		else {
			serviceTime = 0;
			reqInService.calculateTimeInSystem(Mainline.time);
			reqInService.setStatus(Status.DROPPED);
			setReqInService(null);
			Mainline.fuckups++;
			Mainline.numdone++;
		}
	}

	public void updateLocation(double XrandPos, double YrandPos){
		setXpos(this.getXpos() + XrandPos);
		setYpos(this.getYpos() + YrandPos);
	}

}
