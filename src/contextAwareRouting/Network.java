package contextAwareRouting;

public class Network {
	
	private boolean[][] adjMat; //Adjacency Matrix
	
	public Network( boolean[][] inputAdjMat){
		setAdjMat(inputAdjMat);
	}

	void setAdjMat(boolean[][] adjMat){
		this.adjMat = adjMat;
	}
	
	boolean[][] getAdjMat(){
		return adjMat;
	}
	
}
