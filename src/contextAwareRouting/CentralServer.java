package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class CentralServer {

	private ArrayList<Node> nodeList = new ArrayList<Node>(); //master user list
	private LinkedList<Integer> queue = new LinkedList<Integer>(); //holds nodes Ids request to be handled 
																	// Node Id is position in nodelist;
	private int[][] inContactMatrix; //Adjacency Matrix

	private int numUsers;
	private int numRelays;
	private int totalNodes;
 

	//private static 

	public CentralServer(ArrayList<UserNode> userList, ArrayList<RelayNode> relayList){
		nodeList.addAll(userList);
		nodeList.addAll(relayList);

		numUsers = userList.size();
		numRelays = relayList.size();
		totalNodes = numUsers+numRelays;

		updateInContactMatrix();
	}

	public void updateInContactMatrix(){
		inContactMatrix = new int [totalNodes][totalNodes];
		for (int i=0; i<totalNodes; i++){
			for (int j=i+1; j<totalNodes; j++){
				boolean inRange = inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos());
					if(inRange){
						//Get queue lengths for each element of nodelist
						int length = nodeList.get(i).getQueue().size();
						inContactMatrix[i][j] = length;
						inContactMatrix[j][i] = length;
					}
			}
		}
	}

	private boolean inRange(int x1, int y1, int x2, int y2){
		//Pythagoras theorem
		double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		//Record distance if in range, 0 otherwise
		if (distance <= Mainline.T)
			return true;
		else
			return false;
	}

	@SuppressWarnings("null")
	public void handleRequests(){
		
		//the time dependent operation of the server goes here. It should end with the server sending a request on it's way
		// How to handle these requests:
		//		-Request for user nodes
		//		-Request for relay nodes
		
		//for all requests
		for(int i = 0; i < queue.size(); i++){
			Node node = getNodeViaID(queue.get(i));
			
			if (node instanceof RelayNode){
				handleRelayNode( (RelayNode) node);
			}
			else if(node instanceof UserNode){
				handleUserNode( (UserNode) node);
			}
		}
		
		//Instantiation of variable handles 
		int	current = queue.getFirst(); //this is the next request in line to be serviced
		//int sourceNodeID = current.getCurrentNodeID(); //this is the current node that the current request is at (current.current, null if request is new, arrived, dropped)
		//int destinationNodeID = current.getDestinationNodeID(); //the next node that the request is heading to. to be determined by algorithm (special condition if about to finish journey)

		//current.setState(3);

	}

	public void addRequest(int nodeId) {
		queue.add(nodeId);
	}

	public int removeRequest() {
		return queue.remove();
	}

	private Node getNodeViaID(int id){
		return nodeList.get(id);
	}
	
	private void handleUserNode(UserNode user){
		
	}
	
	private void handleRelayNode(RelayNode relay){
		
		LinkedList<Request> tmpQueue = relay.getQueue();
		Request toProcess = tmpQueue.getFirst();
		int nextNodeID = getNextNode(toProcess);
		Request.setCurrentNodeID(nextNodeID);
		
	}
	
	private int getNextNode(Request request){
		DijkstrasAlg nextNode = new DijkstrasAlg(inContactMatrix, request.getCurrentNodeID(), 
										request.getDestinationNodeID(), inContactMatrix.length  );
		return nextNode.getPath()[nextNode.getPath().length - 2];
	}
}
