
public class Edge {
	
	private long id;
	private long fromNode, toNode;
	private Double cost;
	
	public Edge(Long edgeId, Long from, Long to, Double cost) {
		this.id = edgeId;
		this.fromNode = from;
		this.toNode = to;
		this.cost = cost;
	}
	
	public long getId() {
		return id;
	}
	
	public Double getCost() {
		return cost;
	}
	
	public long getFromNode() {
		return fromNode;
	}
	
	public long getToNode() {
		return toNode;
	}
}
