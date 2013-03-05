package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Mainline {
	
		private static int T = 0;
		private static final int Xmax = 0;
		private static final int Ymax = 0;
		private static final int numUsers = 0;
		private static final int numRelays = 0;
		private static final int numapps = 0;
		private static final int requestrate = 0;
		private static final int maxtime = 0;
		private static final int commradius = 0;
		private static final String [][] AppPref = new String[0][0];
	
	private static void main(String[] args) {
		 
		 UserNode[] Users = createUserNodes(numUsers);
		 RelayNode[] Relays = createRelayNodes(numRelays);
		 CentralServer Server = new CentralServer(Users, Relays, AppPref, commradius);
		 ArrayList<Integer> Arrival = RandomNumGen.poissonArrivalTimesList(requestrate, maxtime);
		 
	 }
	 
	 private static UserNode[] createUserNodes(int num){
		 
		 //Make master user node list
		 UserNode[] Users = new UserNode[num];
		 
		 //Variables to work with
		 Random Rand = new Random();
		 int x, y;		 
		 
		 //for each node
		 for(int i = 0; i<num; i++){
			 
			 //generate random x,y
			 do {
				 x = (int) (Rand.nextDouble() * 2 * Xmax - Xmax);
				 y = (int) (Rand.nextDouble() * 2 * Ymax - Ymax);
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
		 Random Rand = new Random();
		 int x, y;		 
		 
		 //for each node
		 for(int i = 0; i<num; i++){
			 
			 //generate random x,y
			 do {
				 x = (int) (Rand.nextDouble() * 2 * Xmax - Xmax);
				 y = (int) (Rand.nextDouble() * 2 * Ymax - Ymax);
			 } while (Math.pow(x/Xmax, 2) + Math.pow(y/Ymax, 2) > 1);
			 
			 //generates an empty list for the queue
			 LinkedList <Request> Queue = new LinkedList <Request>();
			 
			 //create new relay node and add to the master list
			 Nodes[i] = new RelayNode(Queue, 0, x, y);
			 
		 }
		 
		 return Nodes;
	 } 
}
