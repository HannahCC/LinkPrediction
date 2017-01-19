package whu.cs.entrance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import whu.cs.util.FileUtils;
import whu.cs.util.Utils;

public class EdgeAddForClassifiy {

	public static void main(String args[]) throws IOException {
		String rootPath = args[0];
		String dataset = args[1];
		boolean isDirected = Boolean.parseBoolean(args[2]);// false
		int n = args.length - 3;
		int[] edgePercents = new int[n];
		String resPath = rootPath + "graphs\\";
		String graphFile = resPath + dataset + ".edgelist";
		System.out.println("resPath : " + resPath);
		System.out.println("graphFile : " + graphFile);
		System.out.print("edgePercents : ");
		for (int i = 0; i < n; i++) {
			edgePercents[i] = Integer.parseInt(args[3 + i]);
			System.out.print(edgePercents[i] + " ");
		}
		System.out.println();

		List<String> nodes = new ArrayList<String>();
		FileUtils.readGraph(graphFile, nodes);
		Map<Integer, List<Integer>> edges = new HashMap<Integer, List<Integer>>();
		int sum = FileUtils.readGraph(graphFile, nodes, edges);
		if (!isDirected)
			sum /= 2;

		Random random = new Random(1);
		for (int edgePercent : edgePercents) {
			int sample = (int) ((float) edgePercent / 100 * sum);
			System.out.println("start to add :" + edgePercent);
			Map<Integer, List<Integer>> edgesCopy = Utils.copyMap(edges);
			int[][] sampled = new int[sample][2];
			for (int i = 0; i < sample; i++) {
				int top = random.nextInt(nodes.size());
				int tail = random.nextInt(nodes.size());
				Utils.addEdge(edgesCopy, top, tail, isDirected);
				sampled[i][0] = top;
				sampled[i][1] = tail;
			}
			FileUtils.saveGraph(resPath + dataset + "B" + edgePercent
					+ ".edgelist", nodes, edgesCopy);
			FileUtils.saveGraph(resPath + dataset + "B" + edgePercent
					+ ".samplededgelist", nodes, sampled, isDirected);

		}
	}
}
