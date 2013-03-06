package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode {
	
	private  LinkedList <Request> Queue;
	private int processing;
	private int xpos;
	private int ypos;
	
	public RelayNode(int inputXpos, int inputYpos){
		//Setups
		Queue = new LinkedList<Request>();
		processing = 0;
		xpos = inputXpos;
		ypos = inputYpos;
	}
	
	public void tick(){
		//logic for moving requests including manipulating the state
	}
	
	public void addRequest(Request Arrival){
		Arrival.setState("QUEUE");
		Queue.add(Arrival);
	}
	
	public int queueLength(){
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
}
