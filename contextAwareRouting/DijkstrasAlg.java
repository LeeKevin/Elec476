package contextAwareRouting;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class DijkstrasAlg {

	private static final int MAXNODES = 50;
	private static final int INFINITY = Integer.MAX_VALUE;
	
	private int source;
	private int destination;
	private int[][] adjWeight;
	int n;
	// int cost;
	int[][] weight = new int[MAXNODES][MAXNODES];
	int[] distance = new int[MAXNODES];
	int[] precede = new int[MAXNODES];


	/**
	 * Find the shortest path across the graph using Dijkstra's algorithm.
	 */

	public DijkstrasAlg (int adjWeight[][], int source, int destination, int num) {
		this.adjWeight = adjWeight;
		this.source = source;
		this.destination = destination;
		this.n = num;
	}
	
	void buildSpanningTree() {

		boolean[] visit = new boolean[MAXNODES];

		for (int i = 0; i < n; i++) {
			distance[i] = INFINITY;
			precede[i] = INFINITY;
		}
		distance[source] = 0;

		int current = source;

		while (current != destination) {

			int distcurr = distance[current];
			int smalldist = INFINITY;
			int k = -1;
			visit[current] = true;

			for (int i = 0; i < n; i++) {
				if (visit[i])
					continue;

				int newdist = distcurr + weight[current][i];

				if (newdist < distance[i]) {
					distance[i] = newdist;
					precede[i] = current;
				}

				if (distance[i] < smalldist) {
					smalldist = distance[i];
					k = i;
				}
			}
			current = k;
		}
	}

	/**
	 * Get the shortest path across a tree that has had its path weights
	 * calculated.
	 */
	int[] getShortestPath() {

		int i = destination;
		int finall = 0;
		int[] path = new int[MAXNODES];

		path[finall] = destination;
		finall++;
		while (precede[i] != source) {
			i = precede[i];
			path[finall] = i;
			finall++;
		}
		path[finall] = source;

		int[] result = new int[finall + 1];
		System.arraycopy(path, 0, result, 0, finall + 1);
		return result;
	}

	/**
	 * Print the result.
	 */
	void displayResult(int[] path) {

		System.out.println("\nThe shortest path followed is : \n");
		for (int i = path.length - 1; i > 0; i--) {
			System.out.println("\t\t( " + path[i] + " ->" + path[i - 1]
					+ " ) with cost = " + weight[path[i]][path[i - 1]]);
		}

		System.out.print("Path: ");
		for (int i = 0; i < path.length; i++) {
			System.out.print(path[i] + ", ");
		}
		System.out.print("\n");

		System.out.print("Cost: ");
		int cost = 0;
		for (int i = 0; i < path.length - 1; i++) {

			cost = cost + weight[path[i]][path[i + 1]];
			System.out.print("(" + path[i] + ", " + path[i + 1] + ") ");
			System.out.print(cost + ", ");
			// System.out.print(distance[i]);
		}
		System.out.println("Cost: " + cost);

		System.out.println("For the Total Cost = " + cost);

	}

	/**
	 * Prompt for a number.
	 */
	int getNumber(String msg) {
		int ne = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("\n" + msg + " : ");
			ne = Integer.parseInt(in.readLine());
		} catch (Exception e) {
			System.out.println("I/O Error");
		}
		return ne;
	}

	/**
	 * Prompt for a tree, build and display a path across it.
	 */
	public int[] SPA() {

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				weight[i][j] = adjWeight[i][j];
			}
		}
		buildSpanningTree();

		int[] path = getShortestPath();
		displayResult(path);

		return path;
	}

}

