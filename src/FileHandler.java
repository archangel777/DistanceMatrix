import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hugedataaccess.*;
import hugedataaccess.util.*;

public class FileHandler {
	
	private static int segmentSize = 1024*1024;
	private static int nThreads = 2;
	private static String dirName = "data";
	
	public static String getTimePretty(long milis) {
		return (milis/60000) + " min, " + (milis%60000)/1000 + " seg e " + milis%1000 + " ms";
	}
	
	public static long getTotalSize(int numberOfNodes) {
		return (long) Math.ceil((numberOfNodes*numberOfNodes*8d)/(segmentSize*nThreads))*segmentSize;
	}
	
	//Saves the list of "fathers" of each node, related to the shortest path with a given source node Id.
	//The name of the file is the source node Id.
	public static void save(Long sourceId, Integer size, DistanceVector vector, DataAccess dataAccess) {
		for (long i = 1; i<=size; i++) {
			long l = vector.getElement(i).getPreviousId();
			dataAccess.setLong(((sourceId-1)/nThreads * size + i - 1) * 8l, l);
		}
	}
	
	//Reads the "source" file and returns the Distance Vector (Only "father" Id is fetched as distance would at least
	//double the amount of space written in disk.
	public static List<Long> load(Long sourceId, Long target, Integer size) {
		long startTime = System.currentTimeMillis();
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + ((sourceId-1)%nThreads+1) + ".mmap", getTotalSize(size), segmentSize);
		
		List<Long> path = new ArrayList<>();
		long current = target.longValue();
		path.add(new Long(current));
		if ((current = dataAccess.getLong(getMMapPosition(sourceId, size, current))) == -1) return null;
		
		while (current != -1) {
			path.add(0, new Long(current));
			current = dataAccess.getLong(getMMapPosition(sourceId, size, current));
		}
		dataAccess.close();
		
		System.out.println("Loading took " + (System.currentTimeMillis() - startTime) + " ms!");
		return path;
	}
	
	public static long getMMapPosition(Long sourceId, Integer size, long i) {
		return ((sourceId-1)/nThreads * size + i - 1) * 8;
	}
	
	public static void loadSystem(final Graph g) {
		List<Runnable> list = new ArrayList<>();
		
		for (int i = 1; i<=nThreads; i++) addRunnableToList(g, list, i);
		
		for (Runnable r: list) new Thread(r).start();
	}
	
	public static void addRunnableToList(final Graph g, List<Runnable> list, final int pos) {
		list.add(new Runnable() {
			
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				int numberOfNodes = g.getNumberOfNodes();
				ensureDirectoryExists(dirName);
				FileUtils.delete(dirName + "/" + ((pos-1)%nThreads+1) + ".mmap");
				DataAccess dataAccess = new MMapDataAccess(dirName + "/" + ((pos-1)%nThreads+1) + ".mmap", getTotalSize(numberOfNodes), segmentSize);
				
				for(long i = pos; i<=numberOfNodes; i+=nThreads) {
					DistanceVector vector = g.runDijkstra(g.getNode(i));

					FileHandler.save(i, g.getNumberOfNodes(), vector, dataAccess);

					printProgress(i, numberOfNodes, pos);
				}
				
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println(pos + " thread processing finished after " + getTimePretty(System.currentTimeMillis() - startTime));
				System.out.println("-------------------------------------------------------------------------------");
				dataAccess.close();
			}
		});
	}
	
	public static void ensureDirectoryExists(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
	}
	
	public static void printProgress(long i, int numberOfNodes, int threadNumber) {
		int progressNumber = Math.max(1, numberOfNodes/200);
		if (i/progressNumber > (i-nThreads)/progressNumber) System.out.println(new DecimalFormat("#.00").format(i*100./numberOfNodes) + "% - T" + threadNumber);
	}
}
