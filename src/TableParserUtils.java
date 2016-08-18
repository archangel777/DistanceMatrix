
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TableParserUtils {
	
	public static Graph getYuriGraph() {
		Graph g = new Graph();
		g.addNode(new Node(1l));
		g.addNode(new Node(2l));
		g.addNode(new Node(3l));
		g.addNode(new Node(4l));
		g.addNode(new Node(5l));
		g.addNode(new Node(6l));
		g.addNode(new Node(7l));
		g.addEdge(new Edge(1l, 1l, 2l, 6.));
		g.addEdge(new Edge(2l, 2l, 3l, 2.));
		g.addEdge(new Edge(3l, 1l, 6l, 3.));
		g.addEdge(new Edge(4l, 2l, 4l, 7.));
		g.addEdge(new Edge(5l, 2l, 7l, 12.));
		g.addEdge(new Edge(6l, 4l, 5l, 11.));
		g.addEdge(new Edge(7l, 5l, 7l, 3.));
		g.addEdge(new Edge(8l, 4l, 6l, 15.));
		return g;
	}
	
	public static Graph getSmallTestGraph() {
		Graph g = new Graph();
		g.addNode(new Node(1l));
		g.addNode(new Node(2l));
		g.addNode(new Node(3l));
		g.addNode(new Node(4l));
		g.addNode(new Node(5l));
		g.addNode(new Node(6l));
		g.addEdge(new Edge(1l, 1l, 2l, 1.));
		g.addEdge(new Edge(2l, 1l, 3l, 10.));
		g.addEdge(new Edge(3l, 2l, 3l, 2.));
		g.addEdge(new Edge(4l, 3l, 4l, 3.));
		g.addEdge(new Edge(5l, 3l, 5l, 10.));
		g.addEdge(new Edge(6l, 4l, 5l, 2.));
		g.addEdge(new Edge(7l, 4l, 6l, 20.));
		g.addEdge(new Edge(8l, 5l, 6l, 1.));
		return g;
	}
	
	public static Graph getMediumTestGraph() {
		Graph g = new Graph();
		g.addNode(new Node(1l));
		g.addNode(new Node(2l));
		g.addNode(new Node(3l));
		g.addNode(new Node(4l));
		g.addNode(new Node(5l));
		g.addNode(new Node(6l));
		g.addNode(new Node(7l));
		g.addNode(new Node(8l));
		g.addNode(new Node(9l));
		g.addNode(new Node(10l));
		g.addNode(new Node(11l));
		g.addNode(new Node(12l));
		g.addEdge(new Edge(1l, 1l, 2l, 1306.));
		g.addEdge(new Edge(2l, 1l, 5l, 2161.));
		g.addEdge(new Edge(3l, 1l, 7l, 2661.));
		g.addEdge(new Edge(4l, 2l, 3l, 629.));
		g.addEdge(new Edge(5l, 2l, 4l, 919.));
		g.addEdge(new Edge(6l, 3l, 4l, 435.));
		g.addEdge(new Edge(7l, 4l, 5l, 1225.));
		g.addEdge(new Edge(8l, 4l, 6l, 1983.));
		g.addEdge(new Edge(9l, 5l, 6l, 1258.));
		g.addEdge(new Edge(10l, 5l, 7l, 1483.));
		g.addEdge(new Edge(11l, 6l, 7l, 1532.));
		g.addEdge(new Edge(12l, 6l, 9l, 2113.));
		g.addEdge(new Edge(13l, 6l, 10l, 2161.));
		g.addEdge(new Edge(14l, 7l, 8l, 661.));
		g.addEdge(new Edge(15l, 8l, 9l, 1145.));
		g.addEdge(new Edge(16l, 8l, 11l, 1613.));
		g.addEdge(new Edge(17l, 9l, 10l, 1709.));
		g.addEdge(new Edge(18l, 9l, 11l, 725.));
		g.addEdge(new Edge(19l, 9l, 12l, 383.));
		g.addEdge(new Edge(20l, 10l, 12l, 2145.));
		g.addEdge(new Edge(21l, 11l, 12l, 338.));
		return g;
	}
	
	public static Graph getBeijingGraph() {
		return getGraphFromTable("beijing_network.csv");
	}
	
	public static Graph getGraphFromTable(String fileName) {
		CSVReader reader;
		String[] line;
		Graph graph = new Graph();
		
		Long max = 0l;
		try {
			//Opens reader for Beijing table file
			reader = new CSVReader(new FileReader(fileName));
			
			//Finds maximum nodeID while adding all edges to the graph
			while((line = reader.readNext()) != null) {
				Long fromNodeId, toNodeId;
				fromNodeId = Long.valueOf(line[1]);
				toNodeId = Long.valueOf(line[2]);								
				if (fromNodeId>max) max = fromNodeId;
				if (toNodeId>max) max = toNodeId;
			}
			
			//add all nodes to the graph
			for (long i = 1; i<=max; i++) {
				Node node = new Node(i);
				graph.addNode(node);
			}
			reader.close();
			
			reader = new CSVReader(new FileReader(fileName));
			
			//Finds maximum nodeID while adding all edges to the graph
			while((line = reader.readNext()) != null) {
				Long fromNodeId, toNodeId;
				fromNodeId = Long.valueOf(line[1]);
				toNodeId = Long.valueOf(line[2]);
				
				graph.addEdge(new Edge(Long.valueOf(line[0]), fromNodeId, toNodeId, Double.valueOf(line[3])));
			}
			reader.close();
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graph;
	}
	
	public static int parseDoubleStringToInt(String doubleString) {
		Double dNumber = Double.valueOf(doubleString);
		int parsedInt = (int) (dNumber*10000.);
		return parsedInt;
	}
	
}