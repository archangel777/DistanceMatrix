import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

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
			if (element.getId() == source.getId()) {
				element.changeDistance(0.);
			}
			vector.addElement(element);
		}
	}
	
	//Loads the source file and reconstruct the path based on the nodes fathers.
	public List<Long> getShortestPath(Long source, Long target) {		
		return FileHandler.load(source, target, this.getNumberOfNodes());
	}
	
	public Double getPathCost(List<Long> path) {
		Double cost = 0.;
		for (int i = 1; i<path.size(); i++) {
			cost += getNode(path.get(i-1)).getCostToNode(path.get(i));
		}
		return cost;
	}
	
}
