package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class CentralServer {

	private ArrayList<Node> nodeList = new ArrayList<Node>(); 		//master user list
	private LinkedList<Integer> queue = new LinkedList<Integer>(); 	//holds nodeID requests to be handled 
																	// NodeID is position in nodelist;
	private float[][] inContactMatrix; //Adjacency Matrix

	private int numUsers;
	private int numRelays;
	private int totalNodes;

	public CentralServer(ArrayList<UserNode> userList, ArrayList<RelayNode> relayList){
		//create server with generated lists
		nodeList.addAll(userList);
		nodeList.addAll(relayList);

		//helpfull constants for future calculations
		numUsers = userList.size();
		numRelays = relayList.size(); 
		totalNodes = numUsers+numRelays;

		updateInContactMatrix();
	}

	//The non context aware version
	public void updateInContactMatrix(){
		//New matrix to fill
		inContactMatrix = new float [totalNodes][totalNodes];
		
		//nested loops to cycle the matrix
		for (int i=0; i<totalNodes; i++){
			for (int j=i; j<totalNodes; j++){
				//Only fills a number if transition is between two nodes which are within distance t of each other
				if((i!=j) && inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos())){
					//Value is queue size plus 1 which is roughly proportional to time it takes to deploy said request
					inContactMatrix[i][j] = nodeList.get(j).getQueueSize() + 1;
					inContactMatrix[j][i] = nodeList.get(i).getQueueSize() + 1;
				} else {
					//else value is infinity
					inContactMatrix[i][j] = Float.POSITIVE_INFINITY;
					inContactMatrix[j][i] = Float.POSITIVE_INFINITY;
				}
			}
		}
	}
	
	//The context aware version
	public void updateContextMatrix(Request current){
		//New matrix to fill
		inContactMatrix = new float [totalNodes][totalNodes];
		
		//nested loops to cycle the matrix
		for (int i=0; i<totalNodes; i++){
			for (int j=i; j<totalNodes; j++){
				//Only fills a number if transition is between two nodes which are within distance t of each other
				if((i!=j) && inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos())){
					//Value is queue size plus time  which is roughly proportional to time it takes to deploy said request
					inContactMatrix[i][j] = nodeList.get(j).getQueueSize() + (float)nodeList.get(j).calculateServiceRate(current);
					inContactMatrix[j][i] = nodeList.get(i).getQueueSize() + (float)nodeList.get(i).calculateServiceRate(current);
				} else {
					//else value is infinity
					inContactMatrix[i][j] = Float.POSITIVE_INFINITY;
					inContactMatrix[j][i] = Float.POSITIVE_INFINITY;
				}
			}
		}
	}

	//check to see if nodes can communicate
	private boolean inRange(double x1, double y1, double x2, double y2){
		//straight pythagorean theorem
		double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		if (distance <= Mainline.T)
			return true;
		else
			return false;
	}

	public void handleNodeRequests(){
		//if not aware, do dumb version
		if (!Mainline.aware){
			//update the system matrix
			updateInContactMatrix();
			
			//cycle through every node that asked for a next path and handle it
			while(!queue.isEmpty()){
				handleNode(nodeList.get(queue.remove()));
			}
		//else do in context version
		}else{
			while(!queue.isEmpty()){
				handleInContext(nodeList.get(queue.remove()));
			}
		}
		

	}

	private void handleNode(Node node) {
		//Give node a next destination and notify it that it can proceed
		node.setNextNodeID(getNextNode(node.getNodeID(), node.getReqInService().getDestinationNodeID()));
	}
	
	private void handleInContext(Node node) {
		//Give node a next destination and notify it that it can proceed
		updateContextMatrix(node.getReqInService());
		node.setNextNodeID(getNextNode(node.getNodeID(), node.getReqInService().getDestinationNodeID()));
	}

	private int getNextNode(int currentNodeID, int destinationNodeID) {
		//todo: switchcase for all our methods
		return DickAlg(currentNodeID, destinationNodeID);
	}

	public void addNodeRequest(int nodeId) {
		queue.add(nodeId);
	}

	public Node retrieveNode(int NodeID){
		return nodeList.get(NodeID);
	}
	
	private int DickAlg(int source, int destination){
		//define infinity as the max value of an int
		final float INFINITY = Float.POSITIVE_INFINITY;
		
		//local variables to play with
		float[] distance = new float[totalNodes];
		int[] precede= new int[totalNodes];
		int current;
		float testDist;
		int answer = destination;
		boolean success = false;
		
		//Create a list of all the viable nodes (source, destination and all relays)
		ArrayList<Integer> Q = new ArrayList<Integer>();
		Q.add(source);
		Q.add(destination);
		for (int i = numUsers; i < totalNodes; i++)
			Q.add(i);
		
		//Start the distance list off at infinity except source which is 0
		for (int i = 0; i < totalNodes; i++)		
			distance[i] = INFINITY;
		distance[source] = 0;
		
		//Cycle through all viable nodes
		while(!Q.isEmpty()){
			//Picks the closest out of the remaining nodes and removes it
			current = findSmallest(distance, Q);
			Q.remove((Integer)current);
			
			if (distance[current] == INFINITY){
				break;
			}
			
			//If destination is neighbor deals with it
			testDist = distance[current] + inContactMatrix[current][destination];
			if (testDist < distance[destination]){                         
				distance[destination] = testDist ;
				precede[destination] = current;
				success = true;
				break;
			}
			
			//Deals with all relay neighbors
			for (int i = numUsers; i < totalNodes; i++){
				testDist = distance[current] + inContactMatrix[current][i];
				if (testDist < distance[i]){                         
					distance[i] = testDist;
					precede[i] = current; 
				}
			}			
		}
		
		if (success){
			while(precede[answer] != source){
				answer = precede[answer];
			}
			
			return answer;
		}else{
			return -1;
		}
	}
	
	private int findSmallest(float[] list, ArrayList<Integer> Q){
		int x = Q.get(0);
		for (int i : Q){
			if (list[i] < list[x])
				x = i;
		}
		
		return x;
	}
}
