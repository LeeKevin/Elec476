package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode extends Node{

	private int processing = 0;

	public RelayNode(int nodeID, int xpos, int ypos) {
		super(nodeID, xpos, ypos);
	}

	public RelayNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue) {
		super(nodeID, xpos, ypos, queue);
	}

	public void tick(){
		//If processing, count down the ticks until done then send request to server
		if (processing > 1){
			processing --;
		}else if(processing == 1) {
			processing--;
			//			Mainline.server.addRequest(super.removeRequest());
			//If Idle then check if there is anything in the queue and start processing
		}else if(processing ==-1 && super.getQueueSize()>0){
			processing = (int) Mainline.generator.nextExp(Mainline.processrate);
			super.getNextRequest().setState(2);
		}
	}

	public void doneRequest(){
		if (super.getQueueSize()>0){
			processing = (int) Mainline.generator.nextExp(Mainline.processrate);
			super.getNextRequest().setState(2);
		} else {
			processing = -1;
		}
	}

	public int getProcessing(){
		return processing;
	}
}
