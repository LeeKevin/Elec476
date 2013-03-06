package contextAwareRouting;

import java.util.LinkedList;

public class CentralServer {
	
	private UserNode[] userList; //master user list
	private RelayNode[] relayList; //master relay list
	private LinkedList <Request>queue;
	private int[][] adjMat; //Adjacency Matrix
	
	public CentralServer(UserNode[] Users, RelayNode[] Relays){
		
		//Setup attributes
		userList = Users;
		relayList = Relays;
		
		//create weighted adjacency matrix
		createAdjMat();
	}
	
	//creates a weighted adjacency matrix
	private void createAdjMat(){
		//Loops through all nodes
		for (int i=0; i<userList.length; i++){
			//user node to user node first (top left of matrix)
			for (int j=0; j<userList.length; j++){
				adjMat[i][j] = inrange(userList[i].getXpos(), userList[i].getYpos(), userList[j].getXpos(), userList[j].getYpos());
			}
			//user node to relay node (top right)
			for (int j=0; j<relayList.length; j++){
				adjMat[i][userList.length+j] = inrange(userList[i].getXpos(), userList[i].getYpos(), relayList[j].getXpos(), relayList[j].getYpos());
			}
		}
		
		for (int i=0; i<relayList.length; i++){
			//relay node to user node (bottom left)
			for (int j=0; j<userList.length; j++){
				adjMat[userList.length+i][j] = inrange(userList[i].getXpos(), userList[i].getYpos(), userList[j].getXpos(), userList[j].getYpos());
			}
			//relay node to relay node (bottom right)
			for (int j=0; j<relayList.length; j++){
				adjMat[userList.length+i][userList.length+j] = inrange(userList[i].getXpos(), userList[i].getYpos(), relayList[j].getXpos(), relayList[j].getYpos());
			}
		}
	}
	
	//adds a request to the processing queue
	public void addRequest(Request newR){
		queue.add(newR);
	}
	
	private static int inrange(int x1, int y1, int x2, int y2){
		//Pythagoras theorem
		double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		//Record distance if in range, 0 otherwise
		if (distance <= Mainline.T)
			return (int) distance;
		else
			return 0;
	}
	
	public void tick(){
		//the time dependent operation of the server goes here. It should end with the server sending a request on it's way
		
		//Instantiation of variable handles 
		Request	current; //this is the next request in line to be serviced
		RelayNode source; //this is the current node that the current request is at (current.current, null if request is new, arrived, dropped)
		RelayNode destination; //the next node that the request is heading to. to be determined by algorithm (special condition if about to finish journey)
		

		//Algorithm goes here
		
		
		//this is how it needs to end in order to interface properly
		//start (user to relay)
		destination.addRequest(current);
		
		//middle (relay to relay)
		source.doneRequest();
		destination.addRequest(current);
		
		//end (relay to user)
		source.doneRequest();
		current.setState(3);
		
	}

	/*
	void setUserNode( UserNode[] userList){
		this.userList  = userList;
	}
	
	void setRelayNode( RelayNode[] relayList){
		this.relayList = relayList;
	}
	
	void setAppPreference(String[][] appPreference){
		this.appPreference = appPreference;
	}
	
	void setAdjMat(boolean[][] adjMat){
		this.adjMat = adjMat;
	}
	
	public UserNode[] getUserList(){
		return userList;
	}
	
	public RelayNode[] getRelayList(){
		return relayList;
	}
	
	public String[][] appPreference(){
		return appPreference;	
	}
	
	boolean[][] getAdjMat(){
		return adjMat;
	}
	*/
}
