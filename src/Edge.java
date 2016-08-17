
public class Edge {
	
	private Long id;
	private Long fromNode, toNode;
	private Double cost;
	
	public Edge(Long edgeId, Long from, Long to, Double cost) {
		this.id = edgeId;
		this.fromNode = from;
		this.toNode = to;
		this.cost = cost;
	}
	
	public Long getId() {
		return id;
	}
	
	public Double getCost() {
		return cost;
	}
	
	public Long getFromNode() {
		return fromNode;
	}
	
	public Long getToNode() {
		return toNode;
	}
}
