
import java.text.DecimalFormat;
import java.util.List;

public class Main {
	public static void printPath(List<Long> path) {
		if (path == null) {
			System.out.println("There's no path between them!");
		}
		else {
			for (Long id : path) {
				System.out.print(" -> " + id);
			}
			System.out.println();
		}
	}
	
	public static void loadSystem(Graph g) {
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		for(long i = 1; i<=g.getNumberOfNodes(); i++) {
			FileHandler.save(i, g.getNumberOfNodes(), g.runDijkstra(g.getNode(i)));
			if (i%10 == 0) System.out.println(numberFormat.format(i*100./g.getNumberOfNodes()) + "%");
		}
	}
	
	public static void main(String[] args) {
		//Choose a graph to run the application.
		//Graph g = TableParserUtils.getBeijingGraph();
		//Graph g = TableParserUtils.getSmallTestGraph();
		//Graph g = TableParserUtils.getMediumTestGraph();
		Graph g = TableParserUtils.getYuriGraph();
		
		long startTime = System.currentTimeMillis();
		
		//This creates the distance table in disk (run before asking for the shortest path)
		//loadSystem(g);
		
		printPath(g.getShortestPath(2l, 6l));
		
		System.out.println("Processing finished after " + (System.currentTimeMillis() - startTime) + " ms!");
	}
}
