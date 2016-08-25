import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dataAccess.DataAccess;
import dataAccess.MMapDataAccess;

public class FileHandler {
	
	public static String getTimePretty(long milis) {
		return (milis/60000) + " min, " + (milis%60000)/1000 + " seg e " + milis%1000 + " ms";
	}
	
	//Saves the list of "fathers" of each node, related to the shortest path with a given source node Id.
	//The name of the file is the source node Id.
	public static void save(Long sourceId, Integer size, DistanceVector vector) {
		String dirName = "vectors/" + (sourceId/1000 + 1) + "/" + (sourceId/100 + 1);
		ensureDirectoryExists(dirName);
		//useRawMethodForSaving(dir, sourceId, size, vector);
		useMemoryMapForSaving(dirName, sourceId, size, vector);
	}
	
	public static void useMemoryMapForSaving(String dirName, Long sourceId, Integer size, DistanceVector vector) {
		int fileSize = 1024*256;
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + sourceId + ".txt", fileSize);
		dataAccess.ensureCapacity(fileSize);
		for (long i = 0l; i<size; i++) {
			long l = vector.getElement(i+1).getPreviousId();
			dataAccess.setLong(i, l);
		}
		dataAccess.close();
	}
	
	public static void useRawMethodForSaving(File dir, Long sourceId, Integer size, DistanceVector vector) {
		File newFile = new File(dir, sourceId + ".txt");
		try {
			PrintWriter writer = new PrintWriter(newFile);
			DistanceElement element;
			for (long i = 1l; i<=size; i++) {
				element = vector.getElement(i);
				writer.print(element.getPreviousId() + "\n");
			}
			element = vector.getElement(size.longValue());
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
		long startTime = System.currentTimeMillis();
		String dirName = "vectors/" + (sourceId/1000 + 1) + "/" + (sourceId/100 + 1);
		ensureDirectoryExists(dirName);
		System.out.println("Everything took " + (System.currentTimeMillis() - startTime) + " ms!");
		//return useRawMethodForLoading(dir, sourceId, size);
		return useMemoryMapForLoading(dirName, sourceId, size);
	}
	
	public static DistanceVector useMemoryMapForLoading (String dirName, Long sourceId, Integer size) {
		int fileSize = 1024*256;
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + sourceId + ".txt", fileSize);
		dataAccess.ensureCapacity(fileSize);

		DistanceVector vector = new DistanceVector();
		for (long i = 0l; i<size; i++) {
			DistanceElement element = new DistanceElement(i+1);
			long l = dataAccess.getLong(i);
			element.changePrevious(l);
			vector.addElement(element);
		}
		dataAccess.close();
		return vector;
	}
	
	public static DistanceVector useRawMethodForLoading (File dir, Long sourceId, Integer size) {
		File file = new File(dir, sourceId + ".txt");
		DistanceVector vector = new DistanceVector();
		try {
			Scanner s = new Scanner(file);
			for (Long i = 1l; i<=size; i++) {
				DistanceElement element = new DistanceElement(i);
				element.changePrevious(Long.valueOf(s.next()));
				vector.addElement(element);
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vector;
	}
	
	public static void ensureDirectoryExists(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
	}
	
	public static void loadSystem(final Graph g) {
		
		List<Runnable> list = new ArrayList<>();
		int nThreads = 4;
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
				Double avgDijkstra = 0., avgSave = 0.;
				long startTime = System.currentTimeMillis(), startDijkstra, startSave;
				int numberOfNodes = g.getNumberOfNodes(), progressNumber = numberOfNodes/500;
				
				for(long i = pos; i<=numberOfNodes; i+=total) {
					
					startDijkstra = System.currentTimeMillis();
					DistanceVector vector = g.runDijkstra(g.getNode(i));
					avgDijkstra = (avgDijkstra*(i-1)+(System.currentTimeMillis() - startDijkstra))/i;
					
					startSave = System.currentTimeMillis();
					FileHandler.save(i, g.getNumberOfNodes(), vector);
					avgSave = (avgSave*(i-1)+(System.currentTimeMillis() - startSave))/i;
					
					if (i/progressNumber > (i-total)/progressNumber) System.out.println(new DecimalFormat("#.00").format(i*100./numberOfNodes) + "% - T" + pos);
				}
				
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println(pos + " thread processing finished after " + getTimePretty(System.currentTimeMillis() - startTime));
				System.out.println("Dijkstra took on average " + getTimePretty(avgDijkstra.longValue()));
				System.out.println("Saving in disk took on average " + getTimePretty(avgSave.longValue()));
				System.out.println("-------------------------------------------------------------------------------");
			}
		});
	}
}
