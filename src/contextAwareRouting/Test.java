package contextAwareRouting;

public class Test {

	public static void main(String[] args) {
		RandomNumGen generator1 = new RandomNumGen(0,10);
		System.out.println(generator1.uniformList(10).toString());
		
		RandomNumGen generator2 = new RandomNumGen();
		System.out.println(generator2.poissonArrivalTimesList(2, 10));
	
	}

}
