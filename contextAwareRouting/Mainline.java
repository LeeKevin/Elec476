package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class Mainline {
	
		public static final int T = 0;
		public static final int Xmax = 0;
		public static final int Ymax = 0;
		public static final int numUsers = 0;
		public static final int numRelays = 0;
		public static final int numapps = 0;
		public static final int requestrate = 0;
		public static final int queuerate = 0;
		public static final int maxtime = 0;
		public static final String [][] AppPref = new String[0][0];
	
		//The source of all things random:
		public static final RandomNumGen Rand = new RandomNumGen();
		
	public static void main(String[] args) {
		
		 //Create nodes at random and central server 
		 UserNode[] Users = createUserNodes(numUsers);
		 RelayNode[] Relays = createRelayNodes(numRelays);
		 CentralServer Server = new CentralServer(Users, Relays);
		 
		 //Create random arrival times
		 ArrayList<Integer> Arrival = Rand.poissonList(requestrate, maxtime);
		 
		 //time setup
		 boolean done = false;
		 int tick = 0;
		 
		 //main simulation time loop
		 while (!done){
			 
			 //create request if its time
			 while (Arrival.remove((Integer) tick)){
				 Server.createRequest();
			 }
				 
			 tick++;	 
		 }
	 }
	 
	 private static UserNode[] createUserNodes(int num){
		 
		 //Make master user node list
		 UserNode[] Users = new UserNode[num];
		 
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
			 Users[i] = new UserNode(Apps, x, y, Queue);
			 
		 }
		 
		 return Users;
	 }	 
	 
	 private static RelayNode[] createRelayNodes( int num ){
		 
		 //Make master relay node list
		 RelayNode[] Nodes = new RelayNode[num]; 
		 
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
			 Nodes[i] = new RelayNode(x, y);
			 
		 }
		 
		 return Nodes;
	 } 
}
