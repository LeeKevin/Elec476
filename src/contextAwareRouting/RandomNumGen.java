package contextAwareRouting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class RandomNumGen {
	
	private double leftLimit;
	private double rightLimit;
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
		
//		RandomNumGen generator2 = new RandomNumGen(-10,10);
//
//		ArrayList<Double> list2 = generator2.uniformList(100);
//		for (int i=0;i<list2.size();i++){
//			System.out.println(list2.get(i));
//		}
		
	}
	
	public RandomNumGen(double leftLimit, double rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}
	
	public RandomNumGen() {
	}

	public ArrayList<Double> uniformList (int size) {
		int i;
		ArrayList<Double> list = new ArrayList<Double>();
		list.clear();
		for (i=0; i<size; i++) {
			list.add(genUniformNum());
		}
		
		return list;
	}
	
	public LinkedList<Integer> poissonList(double arrivalRate, int maxTime) {
		
		LinkedList<Integer> list = new LinkedList<Integer> ();
		
		double last = 0;
	
		do {
			last = last - (1 / arrivalRate) * Math.log(generator.nextDouble());
			list.add((int) (last*(double)100));
		}
		while (last < maxTime);

		list.remove(list.size()-1);
		
		return list;
	}
	
	public double genUniformNum() {
		double r = generator.nextDouble();		
		return r*(rightLimit-leftLimit) + leftLimit;
	}
	
	public double nextDouble(int low, int high) {
		double r = generator.nextDouble();		
		return r*(high-low) + low;
	}
	
	public double nextExp(int rate){
		return (-Math.log(generator.nextDouble())/rate);
	}
	
	public boolean nextBoolean(){
		return (generator.nextBoolean());
	}
	
	public int nextInt(){
		return (generator.nextInt());
	}
	
}
