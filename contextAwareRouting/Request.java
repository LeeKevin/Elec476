package contextAwareRouting;

import java.util.EnumMap;

public class Request {

	//Request description attributes
	private int sourceNodeID;
	private int destinationNodeID;
	private int currentNodeID;
	private int app;
	private boolean inQueue;
	private boolean dropped;
	
	public enum Status{OUTGOING, INCOMING}; //Outgoing - towards destination, Incoming - returning to source
	private Status status;
	
	private enum Statistics{START_TIME, TIME_IN_QUEUE, TIME_IN_SYSTEM}; //Request simulation statistics
	private EnumMap<Statistics,Integer> Data = new EnumMap<Statistics,Integer>(Statistics.class);
	
	public Request(int sourceNodeID, int destinationNodeID, int app, int tick){
		//setup
		this.sourceNodeID = sourceNodeID;
		this.destinationNodeID = destinationNodeID;
		
		this.app = app;
		
		this.setInQueue(false);
		this.setDropped(false);
		
		setStatus(Status.OUTGOING);
		
		Data.put(Statistics.START_TIME, tick);
		Data.put(Statistics.TIME_IN_QUEUE, 0);
		Data.put(Statistics.TIME_IN_SYSTEM, 0);

		setCurrentNodeID(sourceNodeID);
	}

	public int getSourceNodeID() {
		return sourceNodeID;
	}

	public int getDestinationNodeID() {
		return destinationNodeID;
	}

	public int getCurrentNodeID() {
		return currentNodeID;
	}

	public void setCurrentNodeID(int currentNodeID) {
		this.currentNodeID = currentNodeID;
	}

	public int getApp() {
		return app;
	}
	
	public boolean isInQueue() {
		return inQueue;
	}

	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}

	public boolean isDropped() {
		return dropped;
	}

	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void incrementTimeInQueue() {
		Data.put(Statistics.TIME_IN_QUEUE, Data.get(Statistics.TIME_IN_QUEUE) + 1);
	}
	
	public void calculateTimeInSystem(int exitTime) {
		Data.put(Statistics.TIME_IN_SYSTEM, exitTime - Data.get(Statistics.START_TIME));
	}	
}