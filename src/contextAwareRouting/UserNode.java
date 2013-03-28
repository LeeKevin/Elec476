package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;

public class UserNode extends Node{

	private ArrayList<Integer> appList;

	public UserNode(int nodeID, int xpos, int ypos) {
		super(nodeID, xpos, ypos);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue) {
		super(nodeID, xpos, ypos, queue);
		this.appList = new ArrayList<Integer>();		
	}
	public UserNode(int nodeID, int xpos, int ypos, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos);
		this.appList = appList;		
	}
	public UserNode(int nodeID, int xpos, int ypos, LinkedList<Request> queue, ArrayList<Integer> appList) {
		super(nodeID, xpos, ypos);
		this.appList = new ArrayList<Integer>();		
	}

	public ArrayList<Integer> getAppList(){
		return appList;
	} 	
	
	public void addApp(Integer app){
		appList.add(app);
	} 	
	
	public void remove(Integer app){
		if (appList.contains(app))
		    appList.remove(appList.indexOf(app));
	} 	
}
