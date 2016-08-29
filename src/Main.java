
import java.util.List;
import java.util.Random;

public class Main {
	
	public static void printPath(List<Long> path) {
		if (path == null) System.out.println("There's no path between them!");
		else {
			for (Long id : path) System.out.print(" -> " + id);
			System.out.println();
		}
	}
	
	public static void printRandomPath(Graph g) {
		Random r = new Random();
		Long l1 = Math.abs(r.nextLong()%g.getNumberOfNodes())+1, l2 = Math.abs(r.nextLong()%g.getNumberOfNodes())+1;
		
		System.out.println(l1 + " to " + l2);
		
		List<Long> path = g.getShortestPath(l1, l2);
		
		printPath(path);
		
		if (path != null) System.out.println("The path's cost is " + g.getPathCost(path));
		path = null;
	}
	
	public static void main(String[] args) {
		//Choose a graph to run the application.
		Graph g = TableParserUtils.getBeijingGraph();
		//Graph g = TableParserUtils.getSmallTestGraph();
		//Graph g = TableParserUtils.getMediumTestGraph();
		//Graph g = TableParserUtils.getYuriGraph();
		
		//This creates the distance table in disk (run before asking for the shortest path)
		//FileHandler.loadSystem(g);
		
		printRandomPath(g);
		g = null;
	}
}
