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
		setInputXpos(inputXpos);
		setInputYpos(inputYpos);
		
	}
	
	void setQueue(LinkedList<Request> queue){
		this.queue = queue; 
	}
	
	void setProcessing(int processing){
		this.processing = processing;
	}
	
	void setInputXpos(int xpos){
		this.xpos = xpos;
	}
	
	void setInputYpos(int ypos){
		this.ypos = ypos;
	}
	
	LinkedList<Request> getQueue(){
		return queue;
	}
	
	int getProcessing(){
		return processing;
	}

}
