
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
		//Graph g = TableParserUtils.getBeijingGraph();
		Graph g = TableParserUtils.getSmallTestGraph();
		//Graph g = TableParserUtils.getMediumTestGraph();
		
		long startTime = System.currentTimeMillis();	
		//loadSystem(g);
		printPath(g.getShortestPath(4l, 3l));
		
		System.out.println("Processing finished after " + (System.currentTimeMillis() - startTime) + " ms!");
	}
}
