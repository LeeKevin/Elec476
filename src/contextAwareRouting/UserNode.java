package contextAwareRouting;

//import java.util.ArrayList;
import java.util.LinkedList;

public class UserNode {
	
	private boolean appList[];
	private  LinkedList <Integer>queue;
	private int xpos;
	private int ypos;
	
	public UserNode( boolean inputAppList[], int inputXpos, int inputYpos, LinkedList <Integer>inputQueue){
		
		setAppList(inputAppList);
		setXpos(inputXpos);
		setYpos(inputYpos);
		setQueue(inputQueue);
		
	}

	public void setAppList( boolean[] appList){
		this.appList = appList;
	}
	
	public void setXpos(int xpos){
		this.xpos = xpos;
	}
	
	public void setYpos(int ypos){
		this.ypos = ypos;
	}
	
	public void setQueue(LinkedList <Integer>queue){
		this.queue = queue;
		
	}
	
	public boolean[] getAppList(){
		return appList;
	}
	
	public int getXpos(){
		return xpos;
	}
	
	public int getYpos(){
		return ypos;
	}
	
	public LinkedList<Integer> getQueue(){
		return queue;
	}
 	
}
