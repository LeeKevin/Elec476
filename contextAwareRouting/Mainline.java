package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class Mainline {
	
	//System parameters
	public static final int T = 10;
	public static final int R = 20;
	public static final int numUsers = 10;
	public static final int numRelays = 10;
	public static final int numApps = 3;
	public static final int requestrate = 1;
	public static final int processrate = 0; //in arrivals per second
	public static final int maxtime = 10; //in seconds
	public static final String [][] appPref = new String[0][0];

	//System attributes
	private static ArrayList<UserNode> userList = createUserNodes(numUsers, numApps);
	private static ArrayList<RelayNode> relayList = createRelayNodes(numRelays);
	private static ArrayList<Request> requestList = new ArrayList<Request>();

	public static CentralServer server = new CentralServer(userList, relayList);
	public static final RandomNumGen generator = new RandomNumGen();
	public static int time;

	public void main(String[] args) {

		//Create random arrival times
		LinkedList<Integer> arrivalTimes = generator.poissonList(requestrate, maxtime); //arrival times are given in milliseconds

		boolean done = false;

		//main simulation time loop
		for (time = 0; !done; time++) {

			if (time == arrivalTimes.getFirst()) {
				createRequest();
				arrivalTimes.remove();
			}

			for (Request request: requestList) {
				if (request.isInQueue())
					request.incrementTimeInQueue();
			}

			//Initialize running of network here

			//Graphics generation goes here

			time++;	 
		}
	}

	private static ArrayList<UserNode> createUserNodes(int numUsers, int numApps){
		//variables to work with
		int x, y;		 
		ArrayList<UserNode> userList = new ArrayList<UserNode>(numUsers);

		//for each node
		for(int i = 0; i<numUsers; i++){

			//generate random x,y
			do {
				x = (int) generator.nextDouble(-R, R);
				y = (int) generator.nextDouble(-R, R);
			} while (Math.pow(x/R, 2) + Math.pow(y/R, 2) > 1);

			//generates new array for each node
			ArrayList<Integer> appList = new ArrayList<Integer>();

			//fills it randomly
			for (int j = 0; j<numApps; j++){
				appList.add(generator.nextInt());
			}

			//create new user node and add to the master list
			userList.add(new UserNode(i, x, y, appList));
		}

		return userList;
	}	 

	private static ArrayList<RelayNode> createRelayNodes( int numRelays ){
		//Variables to work with
		int x, y;		 
		ArrayList<RelayNode> relayList = new ArrayList<RelayNode>(numRelays);

		//for each node
		for(int i = 0; i<numRelays; i++){

			//generate random x,y
			do {
				x = (int) generator.nextDouble(-R, R);
				y = (int) generator.nextDouble(-R, R);
			} while (Math.pow(x/R, 2) + Math.pow(y/R, 2) > 1);

			//create new relay node and add to the master list
			relayList.add(new RelayNode(userList.size() + i, x, y));
		}

		return relayList;
	}

	public void createRequest(){
		int sourceNodeID = (int) Math.round(Mainline.generator.nextDouble(0, numUsers -1));
		int destinationNodeID;

		do{ //pick second user that is not the source
			destinationNodeID = (int) Math.round(Mainline.generator.nextDouble(0, numRelays -1));
		} while(sourceNodeID == destinationNodeID);

		Request request = new Request(sourceNodeID, destinationNodeID, (int) Math.round(Mainline.generator.nextDouble(0, Mainline.numApps)), time);
		requestList.add(request);

		//send the request to source node
		server.retrieveNode(sourceNodeID).addRequest(request);
	}
}
