package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode {
	//Node attributes
	private int id;
	
	private  LinkedList <Request> Queue;
	private int processing;
	private int xpos;
	private int ypos;
	
	public RelayNode(int inputXpos, int inputYpos, int id){
		//Setups
		Queue = new LinkedList<Request>();
		processing = 0;
		xpos = inputXpos;
		ypos = inputYpos;
	}
	
	public void tick(){
		//If processing, count down the ticks until done then send request to server
		if (processing > 1){
			processing --;
		}else if(processing == 1) {
			processing--;
			Mainline.Server.addRequest(Queue.remove());
		//If Idle then check if there is anything in the queue and start processing
		}else if(processing ==-1 && Queue.size()>0){
			processing = (int) Mainline.Rand.nextExp(Mainline.processrate);
			Queue.element().setState(2);
		}
	}
	
	public void doneRequest(){
		if (Queue.size()>0){
			processing = (int) Mainline.Rand.nextExp(Mainline.processrate);
			Queue.element().setState(2);
		} else {
			processing = -1;
		}
	}
	
	//method for sending a request to the node
	public void addRequest(Request Arrival){
		Arrival.setState(1, this);
		Queue.add(Arrival);
	}
	
	public int getQueue(){
		return Queue.size();
	}
	
	public int getProcessing(){
		return processing;
	}
	
	public int getXpos() {
		return xpos;
	}

	public int getYpos() {
		return ypos;
	}
	
	public int getID() {
		return id;
	}
}
