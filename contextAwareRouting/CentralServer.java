package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class CentralServer {

	private ArrayList<Node> nodeList = new ArrayList<Node>(); //master user list
	private LinkedList<Integer> queue = new LinkedList<Integer>(); //holds nodeID requests to be handled 
	// NodeID is position in nodelist;
	private int[][] inContactMatrix; //Adjacency Matrix

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
		inContactMatrix = new int [totalNodes][totalNodes];
		for (int i=0; i<totalNodes; i++){
			for (int j=i+1; j<totalNodes; j++){
				boolean inRange = inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos());
				if(inRange){
					//Get queue lengths for each element of nodelist
					int length = nodeList.get(i).getQueueSize();
					inContactMatrix[i][j] = length;
					inContactMatrix[j][i] = length;
				}
			}
		}
	}

	private boolean inRange(int x1, int y1, int x2, int y2){
		double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		if (distance <= Mainline.T)
			return true;
		else
			return false;
	}

	public void handleNodeRequests(){
		//the time dependent operation of the server goes here. It should end with the server sending a request on it's way
		for(int i = 0; i < queue.size(); i++){
			Node node = retrieveNode(removeNodeRequest());
			handleNode(node);
		}
	}

	private void handleNode(Node node) {
		int nextNodeID = getNextNode(node.getNextRequest().getCurrentNodeID(),node.getNextRequest().getDestinationNodeID());
		node.setNextNodeID(nextNodeID);
		node.setWaiting(false);
	}

	private int getNextNode(int currentNodeID, int destinationNodeID) {
		DijkstrasAlg nextNode = new DijkstrasAlg(inContactMatrix, currentNodeID, 
				destinationNodeID, inContactMatrix.length  );
		return nextNode.getPath()[nextNode.getPath().length - 2];
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
}
