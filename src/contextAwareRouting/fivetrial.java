package contextAwareRouting;

public class fivetrial {
	public static Mainline mainline;
	
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++){
			mainline = new Mainline();
			mainline.runmain();
		}
		
		//new mail();
	}
}
