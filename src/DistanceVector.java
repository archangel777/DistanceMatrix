import java.util.HashMap;
import java.util.Map;

public class DistanceVector {
	private Map<Long, DistanceElement> vector;
	
	public DistanceVector() {
		vector = new HashMap<>();
	}
	
	public void addElement(DistanceElement element) {
		vector.put(element.getId(), element);
	}
	
	public DistanceElement getElement(Long id) {
		return vector.get(id);
	}
	
	public void print() {
		for (Long i = 1l; i<=vector.size(); i++) {
			System.out.println("Distance to node " + i + ": " + vector.get(i).getDistance() + " | Previous node: " + vector.get(i).getPreviousId());
		}
	}
}
