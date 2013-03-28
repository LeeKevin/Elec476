package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class Mainline {
		//System parameters to play with
		public static final int T = 10;
		public static final int Xmax = 20;
		public static final int Ymax = 20;
		public static final int numUsers = 10;
		public static final int numRelays = 10;
		public static final int numapps = 3;
		public static final int requestrate = 1;
		public static final int processrate = 0;
		public static final int maxtime = 10;
		public static final String [][] AppPref = new String[0][0];
	
		//The source of all things random:
		public static final RandomNumGen Rand = new RandomNumGen();
		
		//System attributes
		private static UserNode[] Users = new UserNode[numUsers];
		private static RelayNode[] Nodes = new RelayNode[numRelays]; 
		public static CentralServer Server;
		private static LinkedList<Request> Requests = new LinkedList<Request>();
		private static int tick;
		
	public static void main(String[] args) {
		 //Create nodes at random and central server 
		 UserNode[] Users = createUserNodes(numUsers);
		 RelayNode[] Relays = createRelayNodes(numRelays);
		 Server = new CentralServer(Users, Relays);
		 
		 //Create random arrival times
		 ArrayList<Double> Arrival = Rand.poissonList(requestrate, maxtime);
		 
		 //time setup
		 boolean done = false;
		 tick = 0;
		 
		 //main simulation time loop
		 while (!done){
			 
			 //create request if its time
			 while (Arrival.remove((Integer) tick)){
				 createRequest();
			 }
			 
			 //other simulation events will be added here like removal of nodes and dropping of requests
			 
			 //Progress the system by one tick
			 for (Request Current : Requests)
				 Current.tick(tick);
			 for (RelayNode Current : Relays)
				 Current.tick();
			 Server.tick();
			 
			 //graphics generation goes here
			 
			//tick! 
			 tick++;	 
		 }
	 }
	 
	 private static UserNode[] createUserNodes(int num){
		 //variables to work with
		 int x, y;		 
		 
		 //for each node
		 for(int i = 0; i<num; i++){
			 
			 //generate random x,y
			 do {
				 x = (int) Rand.nextDouble(-Xmax, Xmax);
				 y = (int) Rand.nextDouble(-Ymax, Ymax);
			 } while (Math.pow(x/Xmax, 2) + Math.pow(y/Ymax, 2) > 1);
			 
			 //generates new array for each node
			 boolean[] Apps = new boolean[numapps];
			 
			 //fills it randomly
			 for (int j = 0; j<numapps; j++){
				 Apps[j] = Rand.nextBoolean();
			 }
			 
			 //generates an empty list for the queue
			 LinkedList <Request> Queue = new LinkedList <Request>();
			 
			 //create new user node and add to the master list
			 Users[i] = new UserNode(Apps, x, y, Queue, i); 
		 }
		 
		 return Users;
	 }	 
	 
	 private static RelayNode[] createRelayNodes( int num ){
		 //Variables to work with
		 int x, y;		 
		 
		 //for each node
		 for(int i = 0; i<num; i++){
			 
			 //generate random x,y
			 do {
				 x = (int) Rand.nextDouble(-Xmax, Xmax);
				 y = (int) Rand.nextDouble(-Ymax, Ymax);
			 } while (Math.pow(x/Xmax, 2) + Math.pow(y/Ymax, 2) > 1);
			 
			 //create new relay node and add to the master list
			 Nodes[i] = new RelayNode(x, y, Users.length + i);
			 
		 }
		 
		 return Nodes;
	 }
	 
	//creates a new request as it arrives at the source user node
	public static void createRequest(){
		//handles for the source and destination users
		UserNode source = Users[(int) Mainline.Rand.nextDouble(0, numUsers -1)];
		UserNode destination;
		
		//picks second user that is not the source
		do{
			destination = Users[(int) Mainline.Rand.nextDouble(0, numRelays -1)];
		}while(source.equals(destination));
		
		//a new request is born
		Request arrival = new Request(source, destination, (int) Mainline.Rand.nextDouble(0, Mainline.numapps), tick);
		
		//Add request to master list
		Requests.add(arrival);
		
		//send the request for processing
		Server.addRequest (arrival);
	}
}
