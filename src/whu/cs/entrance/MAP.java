package whu.cs.entrance;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import whu.cs.util.FileUtils;

public class MAP {

	public static void main(String args[]) throws IOException {
		String rootPath = args[0];
		boolean isDirected = Boolean.parseBoolean(args[1]);
		int fold = Integer.parseInt(args[2]);
		int en = Integer.parseInt(args[3]);
		int[] edgePercents = new int[en];
		String feature = args[4 + en];
		String resPath = rootPath + "linkPredict_" + isDirected + "\\";
		String resFile = resPath + "MAP.txt";
		System.out.println("resPath : " + resPath);
		System.out.println("resFile : " + resFile);
		System.out.println("fold : " + fold);
		System.out.println("edgePercents : ");
		for (int i = 0; i < en; i++) {
			edgePercents[i] = Integer.parseInt(args[4 + i]);
			System.out.print(edgePercents[i] + " ");
		}
		System.out.println();
		System.out.println("feature : " + feature);

		BufferedWriter bw = FileUtils.saveResInit(resFile, feature);

		double hitted = 0;
		for (int edgePercent : edgePercents) {
			String dirname = resPath + edgePercent + "\\";
			bw.write(edgePercent + "\t");
			double mapAvg = 0;
			for (int f = 0; f < fold; f++) {
				System.out.println("start to calculate :" + edgePercent + "/"
						+ f);
				hitted = 0;
				Set<String> sampledEdge = new HashSet<String>();
				String sampledFile = dirname + "graphs\\" + f
						+ "\\sampled.edgelist";
				FileUtils.readSet(sampledFile, sampledEdge);

				List<String> predictEdge = new ArrayList<String>();
				String predictFile = dirname + "predicts\\" + f + "\\"
						+ feature + ".edgelist";
				FileUtils.readList(predictFile, predictEdge);

				double map = 0;
				for (int i = 0, s = predictEdge.size(); i < s; i++) {
					if (sampledEdge.contains(predictEdge.get(i))) {
						hitted++;
						map += hitted / (i+1);
					}
				}
				map /= sampledEdge.size();
				FileUtils.saveRes(bw, map);
				mapAvg += map;
			}
			FileUtils.saveRes(bw, mapAvg / fold);
			bw.write("\r\n");
		}
		FileUtils.saveResClose(bw);
	}
}
