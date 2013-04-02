package contextAwareRouting;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	public static String FileName = "statistics.csv";

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

		boolean queuesEmpty = true;

		//statistical values
		int numRequests = 0;
		double inQueueTime = 0;
		double inNetworkTime = 0;
		time = 0;
		//main simulation time loop
		do {
			queuesEmpty = true;
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
					//inQueueTime = inQueueTime + server.retrieveNode(i).getQueueSize();
					//inNetworkTime = inNetworkTime + server.retrieveNode(i)getInSystemTime()
					System.out.print("Request in Queue Time = [ ");
					for(int j = 0; j < server.retrieveNode(i).getQueueSize(); j++ ) {
						System.out.print(server.retrieveNode(i).getQueue().get(j).getInQueueTime() + " ");
						//inQueueTime = inQueueTime + server.retrieveNode(i).getQueue().get(j).getInQueueTime();
						//inNetworkTime = inNetworkTime  + server.retrieveNode(i).getQueue().get(j).getInSystemTime();
					}
				} else {
					System.out.print("No requests");
				}
				System.out.println();
			}
			//RELAY NODES

			//Graphics generation goes here

			for (int i=0; i<numUsers;i++) {
				if (server.retrieveNode(i).getQueueSize() > 0) {
					queuesEmpty = false;
					break;
				}
			}
			if (queuesEmpty)
				for (int i=0; i<numRelays;i++) {
					if (server.retrieveNode(i+numUsers).getQueueSize() > 0) {
						queuesEmpty = false;
						break;
					}
				}
			if (time > 10 * 100)
				queuesEmpty = true;
			time++;	 
		}
		while (!arrivalTimes.isEmpty() || !queuesEmpty);


		for(int i = 0; i < requestList.size(); i++){
			inNetworkTime = inNetworkTime + requestList.get(i).getInSystemTime();
			inQueueTime = inQueueTime + requestList.get(i).getInQueueTime();
		}

		System.out.println("Total in queue time: " + inQueueTime);
		System.out.println("Total in system time: " + inNetworkTime);
		System.out.println("Total requests: " + numRequests);
		System.out.println("Total Clock Cycles: " + time);

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

	public static void printStats( int node, int time, int clock )
	{	
		try{
			//Open file to right. 
			String data = " This content will append to the end of the file";

			File file = new File(FileName);

			//if file doesnt exists, then create it
			if(!file.exists()){
				file.createNewFile();
			}

			//true = append file
			FileWriter fileWritter = new FileWriter(file.getName(),true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();

			System.out.println("Done");

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static String OSDectector() {

		String rString = "";
		String os = System.getProperty("os.name").toLowerCase();

		if( os.contains("wins") == true ){
			rString =  "windows";
		}
		else if( os.contains("nux") == true || os.contains("nix") == true ){
			rString = "linux";
		}
		else if( os.contains("mac") == true ){
			rString = "mac";
		}

		return rString;

	}

	public static void openFile(File fileName ){

		String os = OSDectector();

		try
		{
			if (os.contains("windows"))
			{
				Runtime.getRuntime().exec(new String[]
						{"rundll32 url.dll,FileProtocolHandler",
						fileName.getAbsolutePath()});
				//return true;
			} 
			else if (os.contains("linux")){
				Runtime.getRuntime().exec(new String[]{"/usr/bin/open", fileName.getAbsolutePath()});
				//return true;
			} 
			else{
				// Unknown OS, try with desktop
				if (Desktop.isDesktopSupported()){
					Desktop.getDesktop().open(fileName);
					//return true;
				}
				else{
					//return false;
				}
			}
		} catch (Exception e){
			e.printStackTrace(System.err);
			//No OS!
		}
	}


}
