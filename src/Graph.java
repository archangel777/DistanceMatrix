import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Graph {
	
	private Map<Long, Node> nodes;
	private List<Edge> edges;
	
	public Graph(){
		nodes = new HashMap<>();
		edges = new ArrayList<>();
	}
	
	public void addNode(Node node) {
		nodes.put(node.getId(), node);
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
		nodes.get(edge.getFromNode()).addAdjacent(edge);
	}
	
	public Node getNode(Long id) {
		return nodes.get(id);
	}
	
	public Integer getNumberOfNodes() {
		return nodes.size();
	}
	
	public Integer getNumberOfEdges() {
		return edges.size();
	}
		
	public DistanceVector runDijkstra(Node source) {
		DistanceVector vector = new DistanceVector();
		Queue<DistanceElement> toVisit = new PriorityQueue<>();
		
		init(source, vector);
		
		toVisit.add(vector.getElement(source.getId()));
		while (!toVisit.isEmpty()) {
			DistanceElement min = toVisit.remove();
			
			if (min.isVisited()) continue;
			min.setVisited(true);
			
			for (Edge e : nodes.get(min.getId()).getAdjacents()) {
				DistanceElement neighbor = vector.getElement(e.getToNode());
				
				Double newDistance = min.getDistance() + e.getCost();
				if (neighbor.getDistance() > newDistance && !neighbor.isVisited()) {
					neighbor.changeDistance(newDistance);
					neighbor.changePrevious(min.getId());
					toVisit.add(neighbor);
				}
			}
		}
		
		return vector;
	}
	
	public void init(Node source, DistanceVector vector) {
		for (Long i = 1l; i<=nodes.size(); i++) {
			DistanceElement element = new DistanceElement(i);
			if (element.getId().equals(source.getId())) {
				element.changeDistance(0.);
			}
			vector.addElement(element);
		}
	}
	
	public long getRandomNodeId() {
		return Math.abs(new Random().nextLong()%this.getNumberOfNodes()) + 1;
	}
	
	public List<Long> getRandomPath() {
		List<Long> path = new ArrayList<>();
		int counter = 0;
		Long currentPoint = Math.abs(new Random().nextLong()%this.getNumberOfNodes()) + 1;
		System.out.println(currentPoint);
		path.add(currentPoint);
		counter++;
		while (counter <= 40 && !this.getNode(currentPoint).getAdjacents().isEmpty()) {
			counter++;
			currentPoint = this.getNode(currentPoint).getAdjacents().get(0).getToNode();
			path.add(currentPoint);
		}
		return path;
	}
}
