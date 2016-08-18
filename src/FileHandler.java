import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
	//Saves the list of "fathers" of each node, related to the shortest path with a given source node Id.
	//The name of the file is the source node Id.
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
	
	//Reads the "source" file and returns the Distance Vector (Only "father" Id is fetched as distance would at least
	//double the amount of space written in disk.
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
	
	public static void loadSystem(final Graph g) {
		
		List<Runnable> list = new ArrayList<>();
		int nThreads = 3;
		for (int i = 1; i<=nThreads; i++) {
			addRunnableToList(g, list, i, nThreads);
		}
		
		for (Runnable r: list) {
			new Thread(r).start();
		}
	}
	
	public static void addRunnableToList(final Graph g, List<Runnable> list, final int pos, final int total) {
		list.add(new Runnable() {
			
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				for(long i = pos; i<=g.getNumberOfNodes(); i+=total) {
					FileHandler.save(i, g.getNumberOfNodes(), g.runDijkstra(g.getNode(i)));
					if (i%10 == 0) System.out.println(new DecimalFormat("#.00").format(i*100./2000) + "%");
				}
				System.out.println(pos + " thread processing finished after " + (System.currentTimeMillis() - startTime) + " ms!");
			}
		});
	}
}
