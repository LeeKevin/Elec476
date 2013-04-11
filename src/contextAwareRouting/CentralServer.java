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
		nodeList.addAll(userList);
		nodeList.addAll(relayList);

		numUsers = userList.size(); // These may be used for positioning in matrix, up for deletion
		numRelays = relayList.size(); 
		totalNodes = numUsers+numRelays;

		updateInContactMatrix();
	}

	public void updateInContactMatrix(){
		inContactMatrix = new float [totalNodes][totalNodes];
		for (int i=0; i<totalNodes; i++){
			for (int j=i; j<totalNodes; j++){
				if((i!=j) && inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos())){
					inContactMatrix[i][j] = nodeList.get(j).getQueueSize() + 1;
					inContactMatrix[j][i] = nodeList.get(i).getQueueSize() + 1;
				} else {
					inContactMatrix[i][j] = Float.POSITIVE_INFINITY;
					inContactMatrix[j][i] = Float.POSITIVE_INFINITY;
				}
			}
		}
	}

	//check to see if nodes can communicate
	private boolean inRange(double x1, double y1, double x2, double y2){
		double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		if (distance <= Mainline.T)
			return true;
		else
			return false;
	}

	public void handleNodeRequests(){
		//update the system matrix
		updateInContactMatrix();
		//cycle through every node that asked for a next path and handle it
		for(int i = 0; i < queue.size(); i++){
			Node node = retrieveNode(removeNodeRequest());
			handleNode(node);
		}
	}

	private void handleNode(Node node) {
		//Give node a next destination and notify it that it can proceed
		node.setNextNodeID(getNextNode(node.getNodeID(), node.getReqInService().getDestinationNodeID()));
	}

	private int getNextNode(int currentNodeID, int destinationNodeID) {
		//todo: switchcase for all our methods
		return DickAlg(currentNodeID, destinationNodeID);
	}

	public void addNodeRequest(int nodeId) {
		queue.add(nodeId);
	}

	private int removeNodeRequest() {
		return queue.remove();
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
