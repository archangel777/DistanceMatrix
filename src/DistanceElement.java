
public class DistanceElement implements Comparable<DistanceElement>{
	private Long id, previous;
	private Double distance;
	private boolean visited;
	
	public DistanceElement(Long id) {
		this.id = id;
		this.distance = Double.POSITIVE_INFINITY;
		visited = false;
	}
	
	public void changePrevious(Long newPrevious) {
		previous = newPrevious;
	}
	
	public void changeDistance(Double newDistance) {
		distance = newDistance;
	}
	
	public Long getId() {
		return id;
	}
	
	public Double getDistance() {
		return distance;
	}
	
	public Long getPreviousId() {
		return previous;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean b) {
		visited = b;
	}

	@Override
	public int compareTo(DistanceElement o) {
		return Double.compare(this.distance, o.getDistance());
	}
	
}
