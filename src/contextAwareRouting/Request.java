package contextAwareRouting;

public class Request {
	//Request description attributes
	private UserNode Source;
	private UserNode Destination;
	private int App;
	private Node Current = null;
	private enum State{NEW, QUEUE, INSERVICE, ARRIVED, DROPPED};
	private State state;
	
	//Request simulation data, array of int [start time, queue time, system time, dropped]
	private int[] Data = new int[4];
	
	public Request(UserNode source, UserNode destination, int app, int tick){
		//setup
		Source = source;
		Destination = destination;
		App = app;
		state = State.NEW;
		Data[0] = tick;
	}
	
	public void tick(int tick){
		//switch case for counting time depending on the state of the request
		switch (state) {
		case NEW:
			Data[2]++;
			break;
		case QUEUE:
			Data[1]++;
			Data[2]++;
			break;
		case INSERVICE:
			Data[2]++;
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
			Current = null;
			break;
		case 4:
			state = State.DROPPED;
			Current = null;
			Data[3] = 1;
			break;
		default:
			//throw new Exception("Invalid request state");
		}
	}
	
	public void setState(int newState, Node current){
		//overloaded method for changing state to QUEUE
		if (newState==1){
			state = State.QUEUE;
			Current = current;
		}else{
			//throw new Exception("Invalid request state");
		}
	}

	public UserNode getSource() {
		return Source;
	}

	public UserNode getDestination() {
		return Destination;
	}
	
	public int getApp() {
		return App;
	}

	public Node getCurrent() {
		return Current;
	}
	//Class not done, we need to implement setters and getters in a way that will allow the class to keep track of its own stats
}
