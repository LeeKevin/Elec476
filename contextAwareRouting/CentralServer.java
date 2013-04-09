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
				if(i == j){
					inContactMatrix[i][j] = Integer.MAX_VALUE;
					inContactMatrix[j][i] = Integer.MAX_VALUE;
				}
				else if(inRange){
					inContactMatrix[i][j] = nodeList.get(j).getQueueSize();
					inContactMatrix[j][i] = nodeList.get(i).getQueueSize();
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
		//the time dependent operation of the server goes here. It should end with the server sending a request on it's way
		for(int i = 0; i < queue.size(); i++){
			Node node = retrieveNode(removeNodeRequest());
			handleNode(node);
		}
	}

	private void handleNode(Node node) {
		node.setNextNodeID(getNextNode(node.getNextRequest().getCurrentNodeID(),node.getNextRequest().getDestinationNodeID()));
		node.setWaiting(false);
	}

	private int getNextNode(int currentNodeID, int destinationNodeID) {
		DijkstrasAlg alg = new DijkstrasAlg(inContactMatrix, 
											currentNodeID, 
											destinationNodeID, 
											inContactMatrix.length);
		int[] path = alg.SPA();
		if (path != null) {
			int nextNode = path[path.length - 2];
			return nextNode;
		} else {
			return 9999999;
		}
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
