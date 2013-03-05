package contextAwareRouting;

import java.util.ArrayList;
import java.util.Random;

public class RandomNumGen extends Random{
	/*
	private static double leftLimit;
	private static double rightLimit;
	private RNGtype type;
	
	public RandomNumGen(double leftLimit, double rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
		this.type = RNGtype.UNIFORM;
	}
	
	public RandomNumGen() {
		this.type = RNGtype.POISSON;
	}
	*/
	
	public ArrayList<Double> uniformList (int size, int low, int high) {
		int i;
		ArrayList<Double> list = new ArrayList<Double>();
		list.clear();
		for (i=0; i<size; i++) {
			list.add(nextDouble(low, high));
		}
		
		return list;
	}
	
	public ArrayList<Integer> poissonList(int arrivalRate, int maxTime) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.clear();
		list.add(0,(Integer) 0);
		double last = 0;
		
		for (int i=1; last < maxTime; i++) {
			last = (last - (1 / arrivalRate) * Math.log(nextDouble()));
			list.add(i, (Integer) (int) last);
		}
		list.remove(list.size()-1);
		
		return list;
	}
	
	public double nextDouble(int low, int high) {
		double r = nextDouble();		
		return r*(high-low) + low;
	}
	
}
