import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dataAccess.DataAccess;
import dataAccess.FileUtils;
import dataAccess.MMapDataAccess;

public class FileHandler {
	
	private static long totalSize = 1024*317;
	private static int segmentSize = 1024;
	
	public static String getTimePretty(long milis) {
		return (milis/60000) + " min, " + (milis%60000)/1000 + " seg e " + milis%1000 + " ms";
	}

	public static void save(Long sourceId, Integer size, DistanceVector vector) {
		String dirName = "vectors/" + (sourceId/1000 + 1) + "/" + (sourceId/100 + 1);
		ensureDirectoryExists(dirName);
		FileUtils.delete(dirName + "/" + sourceId + ".mmap");
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + sourceId + ".mmap", segmentSize);
		dataAccess.ensureCapacity(totalSize);
		for (long i = 1; i<=size; i++) {
			Double d = vector.getElement(i).getDistance();
			if (d.equals(Double.POSITIVE_INFINITY)) d = -1.;
			
			dataAccess.setDouble(d.doubleValue());
		}
		dataAccess.close();
	}

	public static double getPathCost(Long sourceId, Long targetId) {
		long startTime = System.currentTimeMillis();
		String dirName = "vectors/" + (sourceId/1000 + 1) + "/" + (sourceId/100 + 1);
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + sourceId + ".mmap", segmentSize);
		dataAccess.ensureCapacity(totalSize);

		double cost = dataAccess.getDouble((targetId.longValue()-1)*8);

		dataAccess.close();
		System.out.println("Loading took " + (System.currentTimeMillis() - startTime) + " ms!");
		return cost;
	}
	
	public static List<Double> getListOfPathCosts(Long sourceId, Integer size) {
		//long startTime = System.currentTimeMillis();
		String dirName = "vectors/" + (sourceId/1000 + 1) + "/" + (sourceId/100 + 1);
		DataAccess dataAccess = new MMapDataAccess(dirName + "/" + sourceId + ".mmap", segmentSize);
		dataAccess.ensureCapacity(totalSize);
		List<Double> costList = new ArrayList<>();
		for (long i = 0; i<size; i++) {
			costList.add(dataAccess.getDouble(i*8));
		}
		dataAccess.close();
		//System.out.println("Loading took " + (System.currentTimeMillis() - startTime) + " ms!");
		return costList;
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
				int numberOfNodes = g.getNumberOfNodes(), progressNumber = Math.max(1, numberOfNodes/500);
				
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
