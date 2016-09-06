
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
	
	public static Map<Long, List<Double>> fetched = new HashMap<>();
	
	public static void fetchToMemory(List<Long> path, Integer size) {
		long startTime = System.currentTimeMillis();
		for (Long l : path) {
			fetched.put(l, FileHandler.getListOfPathCosts(l, size));
		}
		System.out.println("Fetching to memory took " + (System.currentTimeMillis() - startTime) + " ms!");
	}
	
	public static Double getCostInMemory(Long sourceId, Long targetId) {
		return fetched.get(sourceId).get(targetId.intValue()-1);
	}
	
	public static void printRandomPath(Graph g) {
		Random r = new Random();
		Long l1 = Math.abs(r.nextLong()%g.getNumberOfNodes())+1, l2 = Math.abs(r.nextLong()%g.getNumberOfNodes())+1;
		
		System.out.println(l1 + " to " + l2);
		
		double cost = FileHandler.getPathCost(l1, l2);
		if (cost == -1) System.out.println("There's no path between them!");
		else System.out.println("The path's cost is " + cost);
	}
	
	public static void main(String[] args) {
		//Choose a graph to run the application.
		Graph g = TableParserUtils.getBeijingGraph();
		//Graph g = TableParserUtils.getSmallTestGraph();
		//Graph g = TableParserUtils.getMediumTestGraph();
		//Graph g = TableParserUtils.getYuriGraph();
		
		//This creates the distance table in disk (run before asking for the shortest path)
		//FileHandler.loadSystem(g);
		
		List<Long> path = g.getRandomPath();
		
		fetchToMemory(path, g.getNumberOfNodes());
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; i<400; i++) {
			for (Long source : path) {
				getCostInMemory(source, g.getRandomNodeId());
			}
		}
		System.out.println((path.size() * 400) + " path costs computed in " + (System.currentTimeMillis() - startTime) + " ms!");
		
		//printRandomPath(g);
		
	}
}
