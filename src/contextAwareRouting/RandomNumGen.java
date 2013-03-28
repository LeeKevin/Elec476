package contextAwareRouting;

import java.util.LinkedList;
import java.util.Random;

public class RandomNumGen {
	
	Random generator = new Random();
	
	public static void main(String[] args) {
		
		boolean done = false;
		for (int i=0; !done; i++) {
			if(i==5) done = true;
		}
		
//		RandomNumGen generator = new RandomNumGen();
//		LinkedList<Integer> list = generator.poissonList(2, 100);
//		for (int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
//		}
		
	}

	public RandomNumGen() {
	}
	
	public LinkedList<Integer> poissonList(double arrivalRate, int maxTime) {
		
		LinkedList<Integer> list = new LinkedList<Integer> ();
		
		double last = 0;
	
		do {
			last = last - nextExp(arrivalRate);
			list.add((int) (last*(double)100));
		}
		while (last < maxTime);

		list.remove(list.size()-1);
		
		return list;
	}
	
	public double genUniformNum(double rightLimit, double leftLimit) {
		double r = generator.nextDouble();		
		return r*(rightLimit-leftLimit) + leftLimit;
	}
	
	public double nextDouble(int low, int high) {
		double r = generator.nextDouble();		
		return r*(high-low) + low;
	}
	
	public double nextExp(double rate){
		return (- (1 / rate) * Math.log(generator.nextDouble()));
	}
	
	public boolean nextBoolean(){
		return (generator.nextBoolean());
	}
	
	public int nextInt(){
		return (generator.nextInt());
	}
	
}
