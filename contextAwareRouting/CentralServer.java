package contextAwareRouting;

public class CentralServer {
	
	private UserNode[] userList;
	private RelayNode[] relayList;
	private String[][] appPreference;
	private boolean[][] adjMat; //Adjacency Matrix
	private static int commradius;
	//private RelayNodes[] shortestPath;
	
	public CentralServer( UserNode[] Users, RelayNode[] Relays, String[][] AppPreference, int commradius){
		
		//Setup attributes
		setUserNode(Users);
		setRelayNode(Relays);
		setAppPreference(AppPreference);
		CentralServer.commradius = commradius;
		
		//create simple adjacency matrix
		createAdjMat();
		
		
	}
	
	private void createAdjMat(){
		//Loops through all nodes
		for (int i=0; i<userList.length; i++){
			for (int j=0; j<userList.length; j++){
				adjMat[i][j] = inrange(userList[i].getXpos(), userList[i].getYpos(), userList[j].getXpos(), userList[j].getYpos(), commradius);
			}
			for (int j=0; j<relayList.length; j++){
				adjMat[i][userList.length+j] = inrange(userList[i].getXpos(), userList[i].getYpos(), relayList[j].getXpos(), relayList[j].getYpos(), commradius);
			}
		}
		
		for (int i=0; i<relayList.length; i++){
			for (int j=0; j<userList.length; j++){
				adjMat[userList.length+i][j] = inrange(userList[i].getXpos(), userList[i].getYpos(), userList[j].getXpos(), userList[j].getYpos(), commradius);
			}
			for (int j=0; j<relayList.length; j++){
				adjMat[userList.length+i][userList.length+j] = inrange(userList[i].getXpos(), userList[i].getYpos(), relayList[j].getXpos(), relayList[j].getYpos(), commradius);
			}
		}
	}
	
	private static boolean inrange(int x1, int y1, int x2, int y2, int r){
		return (Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)) <= (double) r);
	}

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
}
