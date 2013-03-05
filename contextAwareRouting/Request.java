package contextAwareRouting;

public class Request {
	//Request description attributes
	private UserNode Source;
	private UserNode Destination;
	private int app;
	private RelayNode Current;
	
	//Request simulation data, array of int [start time, queue time, system time, dropped]
	private int[] RequestData = new int[4];
	
	public Request(UserNode source, UserNode destination, int app){
		this.Source = source;
		this.Destination = destination;
		this.app = app;
	}
	
	//Class not done, we need to implement setters and getters in a way that will allow the class to keep track of its own stats
}
