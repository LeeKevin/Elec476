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
	public static final int T = 40;
	public static final int R = 200;
	public static final int numUsers = 20;
	public static final int numRelays = 200;
	public static final int numApps = 1; // Must be greater than 0
	public static final double requestrate = 100;
	public static final int maxtime = 1; //in seconds
	public static final String [][] appPref = new String[0][0];
	
	//System attributes
	private static int reqCount = 0;
	public static int time = 0;
	public static final RandomNumGen generator = new RandomNumGen();
	public static String FileName = "statistics.csv";
	private static ArrayList<Request> requestList = new ArrayList<Request>();
	
	//The server placed here so that it is visible to all other classes that need it
	public static CentralServer server;
	
	public static void main(String[] args) {
		//to remove: hard coded end to sim
		boolean end = false;

		//Create random arrival times
		LinkedList<Integer> arrivalTimes = generator.poissonList(requestrate, maxtime); //arrival times are given in milliseconds
		server = new CentralServer(createUserNodes(numUsers, numApps), createRelayNodes(numRelays));

		//to remove: print request times for debug
		System.out.println("Request times:");
		for (int xtime: arrivalTimes){
			System.out.print(xtime + ", ");
		}
		System.out.println();
		
		//statistical values
		double inQueueTime = 0;
		double inNetworkTime = 0;
		
		//needed to end the simulation

		//main simulation time loop
		do {
			//Create a request if it is time to do so
			if (!arrivalTimes.isEmpty())
				while (time == arrivalTimes.getFirst()) {
					createRequest();
					arrivalTimes.remove();
					if (arrivalTimes.isEmpty())
						break;
				}

			//Tick all the nodes
			for (int i=0; i< (numUsers+numRelays); i++)
				server.retrieveNode(i).run();

			//Tick the server
			server.handleNodeRequests();

			//Update request statistics
			for (Request request: requestList) {
				if (request.isInQueue())
					request.incrementTimeInQueue();
			}

			//to remove: Prints the position of all requests + node 0 fuckups
			System.out.println("Statistics for time: " + (double) time/1000 );
			
			Node fuckup = server.retrieveNode(0);
			String cur;
			try{
				cur = Integer.toString(fuckup.getReqInService().getRequestID());
			}catch (Exception e){
				cur = "null";
			}
			
			System.out.print("Node 0 is servicing: " + cur + " and has ");
			for (Request x : fuckup.getQueue()){
				System.out.print(x.getRequestID() + ", ");
			}
			System.out.println("in the queue");
			
			for(Request x : requestList){
				System.out.print("Req:" + x.getRequestID() + " is " + x.getStatus());
				System.out.println(" at node:"+ x.getCurrentNodeID());
			}
			

			//Graphics generation goes here

			//If no more requests to generate and all of them have either arrived or dropped, end sim
			if (arrivalTimes.isEmpty()){
				end = true;
				for (Request r : requestList) {
					if (r.getStatus().equals(Request.Status.OUTGOING) || r.getStatus().equals(Request.Status.INCOMING)) {
						end = false;
						break;
					}
				}
			}
			//to remove: hard coded simulation end condition
			/*if (time > (10 * 1000)){
				end = true;
			}*/
			
			time++;	
			
		}while (!end);

		//Final data output
		for(int i = 0; i < requestList.size(); i++){
			inNetworkTime = inNetworkTime + requestList.get(i).getInSystemTime();
			inQueueTime = inQueueTime + requestList.get(i).getInQueueTime();
		}

		System.out.println("Total in queue time: " + inQueueTime);
		System.out.println("Total in system time: " + inNetworkTime);
		System.out.println("Total requests: " + reqCount);
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
			destinationNodeID = generator.nextInt(0, numUsers -1);
		} while(sourceNodeID == destinationNodeID);

		Request request = new Request(sourceNodeID, destinationNodeID, generator.nextInt(0, Mainline.numApps), reqCount);
		requestList.add(request);
		reqCount++;

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
