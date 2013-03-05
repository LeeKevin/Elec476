package contextAwareRouting;

import java.util.ArrayList;
import java.util.Random;

public class RandomNumGen {
	
	private static double leftLimit;
	private static double rightLimit;
	private RNGtype type;
	private static Random generator = new Random();
	
	public RandomNumGen(double leftLimit, double rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
		this.type = RNGtype.UNIFORM;
	}
	
	public RandomNumGen() {
		this.type = RNGtype.POISSON;
	}

	public static ArrayList<Double> uniformList (int size) {
		int i;
		ArrayList<Double> list = new ArrayList<Double>();
		list.clear();
		for (i=0; i<size; i++) {
			list.add(genUniformNum());
		}
		
		return list;
	}
	
	public static ArrayList<Integer> poissonArrivalTimesList(double arrivalRate, double maxTime) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.clear();
		list.add(0,(Integer) 0);
		double last = 0;
		
		for (int i=1; last < maxTime; i++) {
			last = (last - (1 / arrivalRate) * Math.log(generator.nextDouble()));
			list.add(i, (Integer) (int) last);
		}
		list.remove(list.size()-1);
		
		return list;
	}
	
	public static double genUniformNum() {
		double r = generator.nextDouble();		
		return r*(rightLimit-leftLimit) + leftLimit;
	}
	
}
