package contextAwareRouting;

import java.util.LinkedList;

public class Node {
	private int nodeID;

	private int xpos;
	private int ypos;
	private  LinkedList<Request> queue;

	public Node (int nodeID, int xpos, int ypos) {
		this.nodeID = nodeID;

		this.xpos = xpos;
		this.ypos = ypos;
		this.queue = new LinkedList<Request>();
	}

	public Node (int nodeID, int xpos, int ypos, LinkedList<Request> queue) {
		this(nodeID,xpos,ypos);
		this.queue = queue;
	}

	public int getNodeID() {
		return this.nodeID;
	}

	public int getXpos() {
		return this.xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return this.ypos;
	}

	public void setyPos(int ypos) {
		this.ypos = ypos;
	}

	public Request getNextRequest() {
		return queue.get(0);
	}

	public void addRequest (Request request) {
		request.setState(1, nodeID);
		queue.add(request);
	}
	
	public Request removeRequest() {
		return queue.remove(0);
	}
	
	public Request removeRequest(int i) {
		return queue.remove(i);
	}

	public int getQueueSize() {
		return queue.size();
	}

	public LinkedList<Request> getQueue() {
		return queue;
	}



}
