package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class CentralServer {

	private ArrayList<Node> nodeList = new ArrayList<Node>(); //master user list
	private LinkedList <Request>queue = new LinkedList<Request>();
	private boolean[][] inContactMatrix; //Adjacency Matrix

	private int numUsers;
	private int numRelays;
	private int totalNodes;


	public CentralServer(ArrayList<UserNode> userList, ArrayList<RelayNode> relayList){
		nodeList.addAll(userList);
		nodeList.addAll(relayList);

		numUsers = userList.size();
		numRelays = relayList.size();
		totalNodes = numUsers+numRelays;

		updateInContactMatrix();
	}

	public void updateInContactMatrix(){
		inContactMatrix = new boolean [totalNodes][totalNodes];
		for (int i=0; i<totalNodes; i++){
			for (int j=i+1; j<totalNodes; j++){
				boolean inRange = inRange(nodeList.get(i).getXpos(), nodeList.get(i).getYpos(), nodeList.get(j).getXpos(), nodeList.get(j).getYpos());
				inContactMatrix[i][j] = inRange;
				inContactMatrix[j][i] = inRange;
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
	public void tick(){
		//the time dependent operation of the server goes here. It should end with the server sending a request on it's way

		//Instantiation of variable handles 
		Request	current = queue.getFirst(); //this is the next request in line to be serviced
		int sourceNodeID = current.getCurrentNodeID(); //this is the current node that the current request is at (current.current, null if request is new, arrived, dropped)
		int destinationNodeID = current.getDestinationNodeID(); //the next node that the request is heading to. to be determined by algorithm (special condition if about to finish journey)


		//Algorithm goes here
		//		DijkstrasAlg g = new DijkstrasAlg(adjMat, sourceNodeID, destinationNodeID, adjMat.length);
		//		int[] path = g.SPA();

		//		destination = masterList[path[path.length - 2]];
		//		
		//		//this is how it needs to end in order to interface properly
		//		//start (user to relay)
		//		destination.addRequest(current);
		//		
		//		//middle (relay to relay)
		//		source.doneRequest();
		//		destination.addRequest(current);

		//end (relay to user)
		//		source.doneRequest();
		current.setState(3);

	}

	public void addRequest(Request request) {
		queue.add(request);
	}

	public Request removeRequest() {
		return queue.remove();
	}
}
