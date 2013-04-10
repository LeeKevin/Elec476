package contextAwareRouting;

import java.util.HashSet;


public class Test {

	public static void main(String[] args) {

		    RandomNumGen generator = new RandomNumGen();

			int numApps = 10;
			
			int reqApp = generator.nextInt(0, numApps);
			HashSet<Integer> appList = new HashSet<Integer>();
			do {
				appList.add(generator.nextInt(0, numApps));
				if (appList.size() == 3)
					break;
			} while (true);
			
			
			int minDist = numApps;
			int closestApp = reqApp;
			for (Integer app:appList) {
				int dist = Math.max(0, Math.min(Math.abs(reqApp - app), Math.min(reqApp, app) + numApps - Math.max(reqApp, app)));
				if (dist < minDist) {
					minDist = dist;
					closestApp = app;
				}
				
			}
			double rate = (minDist == 0) ? 2.0 : 1/(0.5 * Math.log((double) minDist) + 1.0);

			System.out.println("Total number of Apps: " + numApps);
			System.out.println("Request App: " + reqApp);
			System.out.println("Apps at destination node: " + appList);

			System.out.println( (int) (generator.nextExp(rate) * 100)); // service time in milliseconds

	}
}
