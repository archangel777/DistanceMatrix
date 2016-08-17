import java.text.DecimalFormat;

public class Main {
	public static void main(String[] args) {
		Graph g = TableParserUtils.getBeijingGraph();
		//Graph g = TableParserUtils.getSmallTestGraph();
		//Graph g = TableParserUtils.getMediumTestGraph();
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		long startTime = System.currentTimeMillis();
		
		for(long i = 1; i<=g.getNumberOfNodes(); i++) {
			FileHandler.save(i, g.getNumberOfNodes(), g.runDijkstra(g.getNode(i)));
			if (i%10 == 0) System.out.println(numberFormat.format(i*100./g.getNumberOfNodes()) + "%");
		}
		
		System.out.println("Processing finished after " + (System.currentTimeMillis() - startTime) + " ms!");
	}
}
