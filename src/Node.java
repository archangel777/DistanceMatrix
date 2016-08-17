import java.util.ArrayList;
import java.util.List;

public class Node {
	private Long id;
	private List<Edge> adjacents;
	
	public Node(Long id) {
		this.id = id;
		adjacents  = new ArrayList<>();
	}
	
	public void addAdjacent(Edge e) {
		adjacents.add(e);
	}
	
	public Long getId() {
		return id;
	}
	
	public List<Edge> getAdjacents() {
		return adjacents;
	}
	
}