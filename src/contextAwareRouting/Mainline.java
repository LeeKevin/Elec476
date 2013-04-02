package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

//import contextAwareRouting.Request.Statistics;

public class Mainline {

	//System parameters
	public static final int T = 15;
	public static final int R = 5;
	public static final int numUsers = 3;
	public static final int numRelays = 3;
	public static final int numApps = 1; // Must be greater than 0
	public static final double requestrate = 10;
	public static final int maxtime = 2; //in seconds
	public static final String [][] appPref = new String[0][0];

	public static final RandomNumGen generator;


	//System attributes
	private static ArrayList<Request> requestList;

	public static CentralServer server;
	public static int time;

	static {
		generator = new RandomNumGen();
		requestList = new ArrayList<Request>();
	}

	public static void main(String[] args) {

		//Create random arrival times
		LinkedList<Integer> arrivalTimes = generator.poissonList(requestrate, maxtime); //arrival times are given in milliseconds
		server = new CentralServer(createUserNodes(numUsers, numApps), createRelayNodes(numRelays));

		boolean done = false;

		//statistical values
		int numRequests = 0;
		int inQueueTime = 0;
		int inNetworkTime = 0;
		time = 0;
		//main simulation time loop
		do {
			if (!arrivalTimes.isEmpty())
				if (time == arrivalTimes.getFirst()) {
					createRequest();
					numRequests++;
					arrivalTimes.remove();
				}

			for (int i=0; i<numUsers; i++)
				server.retrieveNode(i).run();
			for (int i=0; i<numRelays; i++)
				server.retrieveNode(i + numUsers);

			server.handleNodeRequests();

			for (Request request: requestList) {
				if (request.isInQueue())
					request.incrementTimeInQueue();
			}
			//Print Stats
			// Get queue from all nodes
			// Data.get(Statistics.TIME_IN_QUEUE);

			//USER NODES STATS
			System.out.println("Statistics for time: " + (double) time/100 );

			System.out.println("At User Node:");
			for(int i = 0; i < numUsers; i++){
				System.out.print(server.retrieveNode(i).getNodeID() + " : ");
				if (server.retrieveNode(i).getQueueSize() > 0) {
					System.out.print("Request in Queue Time = [ ");
					for(int j = 0; j < server.retrieveNode(i).getQueueSize(); j++ ) {
						System.out.print(server.retrieveNode(i).getQueue().get(j).getInQueueTime() + " ");
					}
				} else {
					System.out.print("No requests");
				}
				System.out.println();
			}
			//RELAY NODES

			//Graphics generation goes here

			time++;	 
		}
		while (time < maxtime * 100);
	}

	private static ArrayList<UserNode> createUserNodes(int numUsers, int numApps){
		//variables to work with
		double x = 0, y = 0;		 
		ArrayList<UserNode> userList = new ArrayList<UserNode>(numUsers);

		//for each node
		for(int i = 0; i<numUsers; i++){

			//generate random x,y
			do {
				x = generator.nextDouble(-R, R);
				y = generator.nextDouble(-R, R);
			} while (Math.pow(x/R, 2) + Math.pow(y/R, 2) > 1);

			//generates new array for each node
			ArrayList<Integer> appList = new ArrayList<Integer>();

			//fills it randomly
			for (int j = 0; j<numApps; j++){
				appList.add(generator.nextInt(1, numApps));
			}

			//create new user node and add to the master list
			userList.add(new UserNode(i, x, y, appList));
		}

		return userList;
	}	 

	private static ArrayList<RelayNode> createRelayNodes( int numRelays ){
		//Variables to work with
		double x = 0, y = 0;		 
		ArrayList<RelayNode> relayList = new ArrayList<RelayNode>(numRelays);

		//for each node
		for(int i = 0; i<numRelays; i++){

			//generate random x,y
			do {
				x = generator.nextDouble(-R, R);
				y = generator.nextDouble(-R, R);
			} while (Math.pow(x/R, 2) + Math.pow(y/R, 2) > 1);

			//create new relay node and add to the master list
			relayList.add(new RelayNode(numUsers + i, x, y));
		}

		return relayList;
	}

	public static void createRequest(){
		int sourceNodeID = generator.nextInt(0, numUsers -1);
		int destinationNodeID;

		do{ //pick second user that is not the source
			destinationNodeID = generator.nextInt(0, numRelays -1);
		} while(sourceNodeID == destinationNodeID);

		Request request = new Request(sourceNodeID, destinationNodeID, generator.nextInt(0, Mainline.numApps), time);
		requestList.add(request);

		//send the request to source node
		server.retrieveNode(sourceNodeID).addRequest(request);
	}
}
