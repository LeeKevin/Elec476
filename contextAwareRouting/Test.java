package contextAwareRouting;

public class Test {

	public static void main(String[] args) {
		int num = 6; // Number of nodes
		int s = 5; // Source node id
		int d = 2; // Destination id
		int adjWeight[][] = { { 1, 1, 1000, 1000, 1, 1000 },
				{ 1, 1000, 1, 1000, 1, 1000 },
				{ 1000, 1, 1000, 1, 1000, 1000 },
				{ 1000, 1000, 1, 1000, 1, 3 }, { 1, 1, 1000, 1, 1000, 1000 },
				{ 1000, 1000, 1000, 3, 1000, 1000 } };

		DijkstrasAlg g = new DijkstrasAlg(adjWeight, s, d, num);
		int[] path = g.SPA();

		System.out.print("Got Path: ");
		String string = "";
		for(int k = 0; k < path.length; k++){
			if (!(string.isEmpty()))
				string += ", ";
			string += path[k];
		}
		System.out.print(string);

	}

}
