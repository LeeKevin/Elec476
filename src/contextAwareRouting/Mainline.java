package contextAwareRouting;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;

//import contextAwareRouting.Request.Statistics;

public class Mainline {

	//System parameters
	public static final int T = 50;
	public static final int R = 50;
	public static final int numUsers = 10;
	public static final int numRelays = 75;
	public static final int numApps = 100; // Must be greater than 0
	public static final double requestrate = 250;
	public static final int maxtime = 5; //in seconds
	public static final boolean aware = false;
	
	//System attributes
	public int reqCount = 0;
	public int time = 0;
	public final RandomNumGen generator = new RandomNumGen();
	public String FileName = "statistics.txt";
	private ArrayList<Request> requestList = new ArrayList<Request>();
	public int fuckups= 0;
	public int dropped= 0;
	public int numdone = 0;
	
	//The server placed here so that it is visible to all other classes that need it
	public static CentralServer server;
	
	public void Mainine(){
		reqCount = 0;
		time = 0;
		RandomNumGen generator = new RandomNumGen();
		ArrayList<Request> requestList = new ArrayList<Request>();
		fuckups= 0;
		dropped= 0;
		numdone = 0;
	}
	
	public void runmain() {
		//End condition
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
			//Tick the server
			server.handleNodeRequests();
			
			//Tick all the nodes
			for (int i=0; i< (numUsers+numRelays); i++)
				server.retrieveNode(i).run();

			//Update request statistics
			for (Request request: requestList) {
				if (request.isInQueue())
					request.incrementTimeInQueue();
			}

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
			
			//Code for moving users around
			for(int i = 0; i < numUsers; i++){
				//displacement
				double tmpX = 0;
				double tmpY = 0;
				//new position
				double tmpXnew = 0;
				double tmpYnew = 0;
				//current position
				double tmpCurrentX = server.retrieveNode(i).getXpos();
				double tmpCurrentY = server.retrieveNode(i).getYpos();
				
				//Make sure that the random number generator creates a valid jump
				do{
					tmpX = generator.nextDouble( (double) (-1)*T, (double) T );
					tmpY = generator.nextDouble( (double) (-1)*T, (double) T );
				
					tmpXnew = tmpCurrentX + tmpX;
					tmpYnew = tmpCurrentY + tmpY;
					
				}while( ( ((tmpX*tmpX) + (tmpY*tmpY)) > (double) (T*T)) || ( ((tmpXnew*tmpXnew) + (tmpYnew*tmpYnew))  > (double) (R*R)) );
				
				//Set new position
				server.retrieveNode(i).setXpos( tmpXnew );
				server.retrieveNode(i).setYpos( tmpYnew );	
			}

			//Increment time
			time++;	
			
		}while (!end);
		
		//statistical values
		BigInteger inQueueTime = BigInteger.ZERO;
		BigInteger inNetworkTime = BigInteger.ZERO;

		//Final data output
		for(int i = 0; i < requestList.size(); i++){
			inNetworkTime = inNetworkTime.add(BigInteger.valueOf(requestList.get(i).getInSystemTime()));
			inQueueTime = inQueueTime.add(BigInteger.valueOf(requestList.get(i).getInQueueTime()));
		}

		System.out.println();
		System.out.println("Total in queue time: " + inQueueTime);
		System.out.println("Total in system time: " + inNetworkTime);
		System.out.println("Total requests: " + reqCount);
		System.out.println("Total Clock Cycles: " + time);
		System.out.println("Total Dropped Request: " + dropped);
		System.out.println("fucked up: " + fuckups);
		
		printStats("Total in queue time: " + inQueueTime + "\n" 
					+ "Total in system time: " + inNetworkTime + "\n" 
					+ "Total requests: " + reqCount + "\n" 
					+ "Total Clock Cycles: " + time + "\n"
					+ "Total Dropped Request: " + dropped + "\n"
					+ "fucked up: " + fuckups + "\n"
					+ "\n"
					+ "\n"
					+ "\n");
	}

	private ArrayList<UserNode> createUserNodes(int numUsers, int numApps){
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

	private ArrayList<RelayNode> createRelayNodes( int numRelays ){
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

			//generates new array for each node
			ArrayList<Integer> appList = new ArrayList<Integer>();

			//fills it randomly
			for (int j = 0; j<numApps; j++){
				appList.add(generator.nextInt(1, numApps));
			}
			
			//create new relay node and add to the master list
			relayList.add(new RelayNode(numUsers + i, x, y, appList));
		}

		return relayList;
	}

	public void createRequest(){
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

	public void printStats(String data)
	{	
		try{
			//Open file to right. 

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
