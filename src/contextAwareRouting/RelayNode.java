package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode {
	
	private  LinkedList <Request> Queue;
	private int processing;
	private int xpos;
	private int ypos;
	
	public RelayNode(int inputXpos, int inputYpos){
		
		Queue = new LinkedList<Request>();
		processing = 0;
		xpos = inputXpos;
		ypos = inputYpos;
		
	}	
	
	int getProcessing(){
		return processing;
	}
	
	public int getXpos() {
		return xpos;
	}

	public int getYpos() {
		return ypos;
	}
}
