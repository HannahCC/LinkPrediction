package whu.cs.entrance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whu.cs.pojo.Edge;
import whu.cs.util.FileUtils;
import whu.cs.util.Utils;

public class LinkPredict_Complete {

	public static void main(String[] args) throws IOException {
		String rootPath = args[0];
		String dataset = args[1];
		boolean isDirected = Boolean.parseBoolean(args[2]);
		int fold = Integer.parseInt(args[3]);
		int k = Integer.parseInt(args[4]);
		int n = Integer.parseInt(args[5]);
		int[] edgePercents = new int[n];
		String feature = args[6 + n];

		String graphFile = rootPath + "graphs\\" + dataset;
		String resPath = rootPath + "linkPredict_" + isDirected + "\\";
		System.out.println("completed graphFile : " + graphFile);
		System.out.println("resPath : " + resPath);
		System.out.println("fold : " + fold);
		System.out.println("k : " + k);
		System.out.print("edgePercents : ");
		for (int i = 0; i < n; i++) {
			edgePercents[i] = Integer.parseInt(args[6 + i]);
			System.out.print(edgePercents[i] + " ");
		}
		System.out.println();
		System.out.println("feature : " + feature);

		if (isDirected) {
			k /= 2;
		}
		
		List<String> nodes = new ArrayList<String>();
		FileUtils.readGraph(graphFile, nodes);
		for (int edgePercent : edgePercents) {
			String dirname = resPath + edgePercent + "\\";
			for (int f = 0; f < fold; f++) {
				System.out
						.println("start to predict :" + edgePercent + "/" + f);
				Map<Integer, List<Integer>> edges = new HashMap<Integer, List<Integer>>();
				FileUtils.readGraph(dirname + "graphs\\" + f
						+ "\\graph.edgelist", nodes, edges);
				Map<String, double[]> nodeVectors = new HashMap<>();
				FileUtils.readVector(dirname + "predicts\\" + f + "\\"
						+ feature + ".emb", nodeVectors);

				int size = nodes.size();
				int id1, id2;
				List<Edge> similars = new ArrayList<Edge>();

				for (int i = 0; i < size; i++) {
					double[] vector1 = nodeVectors.get(nodes.get(i));
					if (vector1 == null)
						continue;
					id1 = Integer.parseInt(nodes.get(i));
					for (int j = i + 1; j < size; j++) {
						if (edges.get(i).contains(j)) {
							continue;
						}
						double[] vector2 = nodeVectors.get(nodes.get(j));
						if (vector2 == null)
							continue;
						double similarity = Utils.cosineSimilarity(vector1,
								vector2);
						id2 = Integer.parseInt(nodes.get(j));
						similars.add(new Edge(id1, id2, similarity));
					}
				}
				Collections.sort(similars);
				FileUtils.saveList(dirname + "predicts\\" + f + "\\" + feature
						+ ".edgelist", similars,isDirected);
			}
		}

	}
}
