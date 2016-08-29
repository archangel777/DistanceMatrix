
public class DistanceElement implements Comparable<DistanceElement>{
	private long id, previous;
	private Double distance;
	private boolean visited;
	
	public DistanceElement(Long id) {
		this.id = id;
		this.distance = Double.POSITIVE_INFINITY;
		previous = -1;
		visited = false;
	}
	
	public void changePrevious(long newPrevious) {
		previous = newPrevious;
	}
	
	public void changeDistance(Double newDistance) {
		distance = newDistance;
	}
	
	public long getId() {
		return id;
	}
	
	public Double getDistance() {
		return distance;
	}
	
	public long getPreviousId() {
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
