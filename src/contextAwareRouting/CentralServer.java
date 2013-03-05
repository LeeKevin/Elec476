package contextAwareRouting;

public class CentralServer {
	
	private UserNode[] userList;
	private RelayNode[] relayList;
	private String[][] appPreference;
	private boolean[][] adjMat; //Adjacency Matrix
	//private RelayNodes[] shortestPath;
	
	public CentralServer( UserNode[] Users, RelayNode[] Relays, String[][] AppPreference){
		
		//Setup attributes
		setUserNode(Users);
		setRelayNode(Relays);
		setAppPreference(AppPreference);
		
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
