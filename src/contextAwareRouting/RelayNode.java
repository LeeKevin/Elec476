package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode {
	
	private  LinkedList <Request>queue;
	private int processing;
	private int xpos;
	private int ypos;
	
	public RelayNode(LinkedList<Request> inputQueue, int inputProcessing, int inputXpos, int inputYpos){
		
		setQueue(inputQueue);
		setProcessing(inputProcessing);
		setXpos(inputXpos);
		setYpos(inputYpos);
		
	}
	
	void setQueue(LinkedList<Request> queue){
		this.queue = queue; 
	}
	
	void setProcessing(int processing){
		this.processing = processing;
	}
	
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}	
	
	LinkedList<Request> getQueue(){
		return queue;
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
