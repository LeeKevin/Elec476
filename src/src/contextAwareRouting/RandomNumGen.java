package contextAwareRouting;

import java.util.LinkedList;
import java.util.Random;

public class RandomNumGen {
	
	Random generator = new Random();

	public RandomNumGen() {
	}
	
	public LinkedList<Integer> poissonList(double arrivalRate, int maxTime) {
		
		LinkedList<Integer> list = new LinkedList<Integer> ();
		
		double last = 0;
	
		do {
			last = last + nextExp(arrivalRate);
			list.add((int) (last*(double)100));
		}
		while (last < maxTime);

		list.remove(list.size()-1);
		
		return list;
	}
	
	public double nextDouble(double low, double high) {
		double r = generator.nextDouble();		
		return r*(high-low) + low;
	}
	
	public int nextInt(int low, int high) {
		return (int) Math.round(nextDouble((double) (low) + 0.499, (double) (high) + 0.499 ));
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
