package whu.cs.entrance;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import whu.cs.util.FileUtils;

public class PrecisionK {

	public static void main(String args[]) throws IOException {
		String rootPath = args[0];
		boolean isConnected = Boolean.parseBoolean(args[1]);
		int fold = Integer.parseInt(args[2]);
		int kn = Integer.parseInt(args[3]);
		int[] ks = new int[kn];
		int en = Integer.parseInt(args[4 + kn]);
		int[] edgePercents = new int[en];
		String feature = args[5 + kn + en];
		String resPath = rootPath + "linkPredict_" + isConnected + "\\";
		String resFile = resPath + "PrecisionAtK.txt";
		System.out.println("resPath : " + resPath);
		System.out.println("resFile : " + resFile);
		System.out.println("fold : " + fold);
		System.out.print("k : ");
		for (int i = 0; i < kn; i++) {
			ks[i] = Integer.parseInt(args[4 + i]);
			System.out.print(ks[i] + " ");
		}
		System.out.println("\nedgePercents : ");
		for (int i = 0; i < en; i++) {
			edgePercents[i] = Integer.parseInt(args[5 + kn + i]);
			System.out.print(edgePercents[i] + " ");
		}
		System.out.println();
		System.out.println("feature : " + feature);

		BufferedWriter bw = FileUtils.saveResInit(resFile, feature, ks);
		double[] precisionAvg = new double[kn];
		double[] precision = new double[kn];
		int hitted = 0;
		for (int edgePercent : edgePercents) {
			String dirname = resPath + "sampled" + edgePercent + "percent\\";
			for (int f = 0; f < fold; f++) {
				System.out.println("start to calculate :" + edgePercent + "/"
						+ f);
				for (int i = 0; i < kn; i++)
					precision[i] = 0;
				hitted = 0;
				Set<String> sampledEdge = new HashSet<String>();
				String sampledFile = dirname + "graphs\\" + f
						+ "\\sampled.edgelist";
				FileUtils.readSet(sampledFile, sampledEdge);

				List<String> predictEdge = new ArrayList<String>();
				String predictFile = dirname + "predicts\\" + f + "\\"
						+ feature + ".edgelist";
				FileUtils.readList(predictFile, predictEdge, ks[kn - 1]);
				int c = 0;
				for (int i = 0, s = predictEdge.size(); i < s; i++) {
					if (sampledEdge.contains(predictEdge.get(i))) {
						hitted++;
					}
					if (i == (ks[c] - 1)) {
						precision[c] = (double) hitted / ks[c];
						c++;
						if (c == kn)
							break;
					}
				}

				FileUtils.saveRes(bw, f + "", precision);
			}
			for (int i = 0; i < kn; i++)
				precisionAvg[i] += precision[i];

		}
		for (int i = 0; i < kn; i++)
			precisionAvg[i] /= fold;
		FileUtils.saveRes(bw, "avg", precisionAvg);
		FileUtils.saveResClose(bw);
	}
}
