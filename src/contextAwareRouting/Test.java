package contextAwareRouting;

public class Test {

	public static void main(String[] args) {
		RandomNumGen generator1 = new RandomNumGen();
		
		System.out.println(generator1.uniformList(10, 0 ,10).toString());
	
		System.out.println(generator1.poissonList(2, 10));
	
	}

}
