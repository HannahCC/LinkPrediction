package whu.cs.entrance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import whu.cs.util.FileUtils;
import whu.cs.util.Utils;

public class EdgeSample {

	public static void main(String args[]) throws IOException {
		String rootPath = args[0];
		String dataset = args[1];
		boolean isConnected = Boolean.parseBoolean(args[2]);
		int fold = Integer.parseInt(args[3]);
		int n = args.length - 4;
		int[] edgePercents = new int[n];
		String graphFile = rootPath + "graphs\\" + dataset;
		String resPath = rootPath + "linkPredict_" + isConnected + "\\";
		System.out.println("graphFile : " + graphFile);
		System.out.println("resPath : " + resPath);
		System.out.println("fold : " + fold);
		System.out.print("edgePercents : ");
		for (int i = 0; i < n; i++) {
			edgePercents[i] = Integer.parseInt(args[4 + i]);
			System.out.print(edgePercents[i] + " ");
		}
		System.out.println();

		List<String> nodes = new ArrayList<String>();
		FileUtils.readGraph(graphFile, nodes);
		Map<Integer, List<Integer>> edges = new HashMap<Integer, List<Integer>>();
		int sum = FileUtils.readGraph(graphFile, nodes, edges);
		if (isConnected && !Utils.isConnected(edges)) {
			System.out.println("this graph is not connected.");
			isConnected = false;
		}

		Random random = new Random(1);
		int count = 0;
		for (int edgePercent : edgePercents) {
			String sampledGraphFile = resPath + "sampled" + edgePercent
					+ "percent\\graphs\\";
			int sample = (int) ((float) edgePercent / 100 * sum);
			for (int f = 0; f < fold; f++) {
				System.out.println("start to sample :" + edgePercent + "/" + f
						+ "/" + sample);
				Map<Integer, List<Integer>> edgesCopy = Utils.copyMap(edges);
				int[][] sampled = new int[sample][2];
				for (int i = 0; i < sample; i++) {
					int top = random.nextInt(nodes.size());
					List<Integer> adjs = edgesCopy.get(top);
					if (adjs == null || adjs.size() < 2) {
						i--;
						continue;
					}
					int tail = adjs.get(random.nextInt(adjs.size()));
					Utils.removeEdge(edgesCopy, top, tail);
					if (count < 3 && isConnected
							&& !Utils.isConnected(edgesCopy)) {
						Utils.addEdge(edgesCopy, top, tail);
						count++;
						i--;
						continue;
					}
					count = 0;
					sampled[i][0] = top;
					sampled[i][1] = tail;
				}
				FileUtils.mkdirs(sampledGraphFile + f + "\\");
				FileUtils.mkdirs(resPath + "sampled" + edgePercent
						+ "percent\\predicts\\" + f + "\\");
				FileUtils.saveGraph(sampledGraphFile + f + "\\graph.edgelist",
						nodes, edgesCopy);
				FileUtils.saveGraph(
						sampledGraphFile + f + "\\sampled.edgelist", nodes,
						sampled);
			}
		}
	}
}
