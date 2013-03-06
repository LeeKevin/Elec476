package contextAwareRouting;

//import java.util.ArrayList;
import java.util.LinkedList;

public class UserNode {
	//Attributes
	private boolean appList[];
	private  LinkedList <Request>queue;
	private int xpos;
	private int ypos;
	
	public UserNode( boolean inputAppList[], int inputXpos, int inputYpos, LinkedList <Request>inputQueue){
		
		appList = inputAppList;
		xpos = inputXpos;
		ypos = inputYpos;
		queue = inputQueue;
		
	}
/* There is no reason to have setters for this class, they can't and shouldn't change
	public void setAppList( boolean[] appList){
		this.appList = appList;
	}
	
	public void setXpos(int xpos){
		this.xpos = xpos;
	}
	
	public void setYpos(int ypos){
		this.ypos = ypos;
	}
	
	public void setQueue(LinkedList <Request>queue){
		this.queue = queue;
		
	}
	
*/	
	public boolean[] getAppList(){
		return appList;
	}
	
	public int getXpos(){
		return xpos;
	}
	
	public int getYpos(){
		return ypos;
	}
	
	public int getQueue(){
		return queue.size();
	}
 	
}
