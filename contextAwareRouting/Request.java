package contextAwareRouting;

import java.util.EnumMap;

public class Request {

	//Request description attributes
	private int sourceNodeID;
	private int destinationNodeID;
	private int currentNodeID;

	private int app;
	private enum State{NEW, QUEUE, INSERVICE, ARRIVED, DROPPED};
	private State state;

	//Request simulation data, array of int [start time, queue time, system time, dropped]
	private enum DataType{STARTTIME, QUEUETIME, SYSTEMTIME, DROPPEDFLAG};
	private EnumMap<DataType,Integer> Data = new EnumMap<DataType,Integer>(DataType.class);
	//	private int[] Data = new int[4];

	public Request(int sourceNodeID, int destinationNodeID, int app, int tick){
		//setup
		this.sourceNodeID = sourceNodeID;
		this.destinationNodeID = destinationNodeID;
		this.app = app;
		state = State.NEW;
		Data.put(DataType.STARTTIME, tick);
		Data.put(DataType.QUEUETIME, 0);
		Data.put(DataType.SYSTEMTIME, 0);
		Data.put(DataType.DROPPEDFLAG, 0);

		currentNodeID = sourceNodeID;
	}

	public void tick(int tick){
		//switch case for counting time depending on the state of the request
		switch (state) {
		case NEW:
			Data.put(DataType.SYSTEMTIME, Data.get(DataType.SYSTEMTIME)+1);
			break;
		case QUEUE:
			Data.put(DataType.QUEUETIME, Data.get(DataType.QUEUETIME)+1);
			Data.put(DataType.SYSTEMTIME, Data.get(DataType.SYSTEMTIME)+1);
			break;
		case INSERVICE:
			Data.put(DataType.SYSTEMTIME, Data.get(DataType.SYSTEMTIME)+1);
			break;
		case ARRIVED:
			break;
		case DROPPED:
			break;	
		default:
			break;
		}

		//other tracking code can go here possibly even with tracking capability due to tick being passed in
	}

	public void setState(int newState){
		//switch case for changing state
		switch (newState) {
		case 2:
			state = State.INSERVICE;
			break;
		case 3:
			state = State.ARRIVED;
			break;
		case 4:
			state = State.DROPPED;
			Data.put(DataType.DROPPEDFLAG, 1);
			break;
		default:
			//throw new Exception("Invalid request state");
		}
	}

	public void setState(int newState, int nodeID){
		//overloaded method for changing state to QUEUE
		if (newState==1){
			state = State.QUEUE;
			currentNodeID = nodeID;
		}else{
			//throw new Exception("Invalid request state");
		}
	}

	public int getSourceNodeID() {
		return sourceNodeID;
	}

	public int getDestinationNodeID() {
		return destinationNodeID;
	}

	public int getApp() {
		return app;
	}

	public int getCurrentNodeID() {
		return currentNodeID;
	}
	//Class not done, we need to implement setters and getters in a way that will allow the class to keep track of its own stats
}
