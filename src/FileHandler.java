import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileHandler {
	public static void save(Long sourceId, Integer size, DistanceVector vector) {
		File dir = new File("vectors");
		dir.mkdirs();
		File newFile = new File(dir, sourceId + ".txt");
		try {
			PrintWriter writer = new PrintWriter(newFile);
			for (long i = 1l; i<size; i++) {
				DistanceElement element = vector.getElement(i);
				writer.print(element.getPreviousId() + "\n");
			}
			DistanceElement element = vector.getElement(size.longValue());
			writer.print(element.getPreviousId());
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DistanceVector load(Long sourceId, Integer size) {
		File dir = new File("vectors");
		dir.mkdirs();
		File file = new File(dir, sourceId + ".txt");
		DistanceVector vector = new DistanceVector();
		try {
			Scanner s = new Scanner(file);
			String aux;
			for (Long i = 1l; i<=size; i++) {
				DistanceElement element = new DistanceElement(i);
				if (!(aux = s.next()).equals("null")) element.changePrevious(Long.valueOf(aux));
				vector.addElement(element);
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vector;
	}
}
