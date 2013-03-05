package contextAwareRouting;

import java.util.LinkedList;

public class RelayNode {
	
	private  LinkedList <Integer>queue;
	private int processing;
	private int xpos;
	private int ypos;
	
	public RelayNode(LinkedList<Integer> inputQueue, int inputProcessing, int inputXpos, int inputYpos){
		
		setQueue(inputQueue);
		setProcessing(inputProcessing);
		setInputXpos(inputXpos);
		setInputYpos(inputYpos);
		
	}
	
	void setQueue(LinkedList<Integer> queue){
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
	
	LinkedList<Integer> getQueue(){
		return queue;
	}
	
	int getProcessing(){
		return processing;
	}

}
