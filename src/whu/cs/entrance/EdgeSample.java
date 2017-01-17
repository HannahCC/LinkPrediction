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
		boolean isDirected = Boolean.parseBoolean(args[2]);
		int fold = Integer.parseInt(args[3]);
		int adjNum = Integer.parseInt(args[4]);
		int n = args.length - 5;
		int[] edgePercents = new int[n];
		String graphFile = rootPath + "graphs\\" + dataset;
		String resPath = rootPath + "linkPredict_" + isDirected + "\\";
		System.out.println("graphFile : " + graphFile);
		System.out.println("resPath : " + resPath);
		System.out.println("fold : " + fold);
		System.out.print("edgePercents : ");
		for (int i = 0; i < n; i++) {
			edgePercents[i] = Integer.parseInt(args[5 + i]);
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
		int count = 0;
		int count_max = adjNum == 0 ? 0 : 3;
		for (int edgePercent : edgePercents) {
			String sampledGraphFile = resPath + edgePercent + "\\graphs\\";
			int sample = (int) ((float) edgePercent / 100 * sum);
			for (int f = 0; f < fold; f++) {
				System.out.println("start to sample :" + edgePercent + "/" + f
						+ "/" + sample);
				Map<Integer, List<Integer>> edgesCopy = Utils.copyMap(edges);
				int[][] sampled = new int[sample][2];
				for (int i = 0; i < sample; i++) {
					int top = random.nextInt(nodes.size());
					List<Integer> adjs = edgesCopy.get(top);
					if (adjs.size() == 0) {
						i--;
						continue;
					}
					if (count < count_max && adjs.size() < adjNum) {
						count++;
						i--;
						continue;
					}
					int tail = adjs.get(random.nextInt(adjs.size()));

					if (count < count_max
							&& edgesCopy.get(tail).size() < adjNum) {
						count++;
						i--;
						continue;
					}
					Utils.removeEdge(edgesCopy, top, tail, isDirected);
					count = 0;
					sampled[i][0] = top;
					sampled[i][1] = tail;
				}
				FileUtils.mkdirs(sampledGraphFile + f + "\\");
				FileUtils.mkdirs(resPath + edgePercent + "\\predicts\\" + f
						+ "\\");
				FileUtils.saveGraph(sampledGraphFile + f + "\\graph.edgelist",
						nodes, edgesCopy);
				FileUtils.saveGraph(
						sampledGraphFile + f + "\\sampled.edgelist", nodes,
						sampled, isDirected);
			}
		}
	}
}
