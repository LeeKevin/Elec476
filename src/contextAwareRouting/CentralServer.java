package contextAwareRouting;

public class CentralServer {
	
	private UserNode[] userList;
	private RelayNode[] relayList;
	private String[][] appPreference;
	//private RelayNodes[] shortestPath;
	
	public CentralServer( UserNode[] totUsers, RelayNode[] totRelays, String[][] totAppPreference){
		
		//Setup attributes
		setUserNode(totUsers);
		setRelayNode(totRelays);
		setAppPreference(totAppPreference);
		
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
	
	public UserNode[] getUserList(){
		return userList;
	}
	
	public RelayNode[] getRelayList(){
		return relayList;
	}
	
	public String[][] appPreference(){
		return appPreference;	
	}
}
