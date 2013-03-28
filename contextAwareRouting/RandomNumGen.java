package contextAwareRouting;

import java.util.ArrayList;
import java.util.Random;

public class RandomNumGen {
	
	private double leftLimit;
	private double rightLimit;
	private RNGtype type;
	Random generator = new Random();
	
	public static void main(String[] args) {
		
//		RandomNumGen generator = new RandomNumGen();
//		ArrayList<Double> list = generator.poissonList(2, 100);
//		for (int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
//		}
		
		RandomNumGen generator2 = new RandomNumGen(-10,10);

		ArrayList<Double> list2 = generator2.uniformList(100);
		for (int i=0;i<list2.size();i++){
			System.out.println(list2.get(i));
		}
		
	}
	
	public RandomNumGen(double leftLimit, double rightLimit) {
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
		this.type = RNGtype.UNIFORM;
	}
	
	public RandomNumGen() {
		this.type = RNGtype.POISSON;
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
	
	public ArrayList<Double> poissonList(double arrivalRate, double maxTime) {
		
		int i=0;
		ArrayList<Double> list = new ArrayList<Double>();
		list.clear();
		list.add(0,(double) 0);
		
		while (list.get(i) < maxTime) {
			i++;
			list.add(i, list.get(i-1) - (1 / arrivalRate) * Math.log(generator.nextDouble()));
		}
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
	
}
