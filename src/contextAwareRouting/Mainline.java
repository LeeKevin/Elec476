package contextAwareRouting;

import java.util.LinkedList;
import java.util.Random;

public class Mainline {
	
		private static int T = 0;
		private static int Xmax = 0;
		private static int Ymax = 0;
		private static int numUsers = 0;
		private static int numRelays = 0;
		private static int numapps = 0;
		private static String [][] AppPref;
	
	 public void main(String[] args) {
		 
		 UserNode[] users = createUserNodes(numUsers);
		 RelayNode[] relays = createRelayNodes(numRelays);
		 CentralServer server = new CentralServer(users, relays, AppPref);
		 
	 }
	 
	 private UserNode[] createUserNodes(int num){
		 
		 UserNode[] User = new UserNode[num]; 
		 Random Rand = new Random();
		 int x, y;		 
		 
		 for(int i = 0; i<num; i++){
			 //generate random x,y
			 do {
				 x = (int) (Rand.nextDouble() * 2 * Xmax - Xmax);
				 y = (int) (Rand.nextDouble() * 2 * Ymax - Ymax);
			 } while (Math.pow(x/Xmax, 2) + Math.pow(y/Ymax, 2) < 1);
			 
			 //generates new array for each node
			 boolean[] Apps = new boolean[numapps];
			 
			 //fills it randomly
			 for (int j = 0; j<numapps; j++){
				 Apps[j] = Rand.nextBoolean();
			 }
			 
			 //generates an empty list for the queue
			 LinkedList <Request> Queue = new LinkedList <Request>();
			 
			 //create new usernode and add to the master list
			 User[i] = new UserNode(Apps, x, y, Queue);
			 
		 }
		 
		 return User;
	 }	 
	 
	 private RelayNode[] createRelayNodes( int num ){
		 
		 RelayNode[] Node = new RelayNode[num]; 
		 
		 Random Rand = new Random();
		 int x, y;		 
		 
		 for(int i = 0; i<num; i++){
			 //generate random x,y
			 do {
				 x = (int) (Rand.nextDouble() * 2 * Xmax - Xmax);
				 y = (int) (Rand.nextDouble() * 2 * Ymax - Ymax);
			 } while (Math.pow(x/Xmax, 2) + Math.pow(y/Ymax, 2) < 1);
			 
			 //generates an empty list for the queue
			 LinkedList <Request> Queue = new LinkedList <Request>();
			 
			 //create new usernode and add to the master list
			 Node[i] = new RelayNode(Queue, 0, x, y);
			 
		 }
		 
		 return Node;
	 }
	 
	 private static Network createNetwork(UserNode[] users, RelayNode[] relays, int num){
		 
		 
		 //each row and column contains the information for the connections.
		 //This matrix should be symmetric. 
		 
		 
		 
		 boolean AMatrix[][] = new boolean[num][num];
		 
		 Network ntwk = new Network(AMatrix);
		 return ntwk;
		 
	 }
	 
	 
}
