package whu.cs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import whu.cs.pojo.Edge;

public class FileUtils {
	static DecimalFormat df = new DecimalFormat("#.00");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public static void readGraph(String graphFile, List<String> nodes)
			throws IOException {
		File f = new File(graphFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int node;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			node = nodes.indexOf(items[0]);
			if (node == -1) {
				node = nodes.size();
				nodes.add(items[0]);
			}
		}
		br.close();
	}

	public static int readGraph(String graphFile, List<String> nodes,
			Map<Integer, List<Integer>> edges) throws IOException {
		int edgeNum = 0;
		File f = new File(graphFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int node0, node1;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			edgeNum++;
			node0 = nodes.indexOf(items[0]);
			node1 = nodes.indexOf(items[1]);
			if (node0 == -1) {
				System.out.println("cannot find node " + node0
						+ "in the arrays.");
				System.exit(-1);
			}
			if (node1 == -1) {
				System.out.println("cannot find node " + node1
						+ "in the arrays.");
				System.exit(-1);
			}
			List<Integer> adjs = null;
			if (!edges.containsKey(node0)) {
				adjs = new ArrayList<Integer>();
				adjs.add(node1);
				edges.put(node0, adjs);
			} else {
				adjs = edges.get(node0);
				if (!adjs.contains(node1))
					adjs.add(node1);
			}

		}
		br.close();
		return edgeNum / 2;
	}

	public static void readVector(String vectorFile,
			Map<String, double[]> vectors) throws IOException {
		File f = new File(vectorFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		String[] items = null;
		line = br.readLine();
		int size = Integer.parseInt(line.split("\\s+")[1]);
		while (null != (line = br.readLine())) {
			items = line.split("\\s+");
			String node = items[0];
			double[] vector = new double[size];
			for (int i = 0; i < size; i++) {
				vector[i] = Double.parseDouble(items[i + 1]);
			}
			vectors.put(node, vector);
		}
		br.close();
	}

	public static void readSet(String filename, Set<String> set)
			throws IOException {

		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while (null != (line = br.readLine())) {
			set.add(line);
		}
		br.close();
	}

	public static void readList(String filename, List<String> list, int size)
			throws IOException {

		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int i = 0;
		while (null != (line = br.readLine())) {
			list.add(line.split("\t")[0]);
			if (++i == size)
				break;
		}
		br.close();
	}

	public static void readList(String filename, List<String> list)
			throws IOException {

		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while (null != (line = br.readLine())) {
			list.add(line.split("\t")[0]);
		}
		br.close();
	}

	public static void saveGraph(String sampledGraphFile, List<String> nodes,
			Map<Integer, List<Integer>> edges) throws IOException {
		File f = new File(sampledGraphFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (Entry<Integer, List<Integer>> entry : edges.entrySet()) {
			String top = nodes.get(entry.getKey());
			if (entry.getValue() == null)
				continue;
			for (int tail : entry.getValue()) {
				bw.write(top + "\t" + nodes.get(tail) + "\r\n");
			}
		}
		bw.flush();
		bw.close();
	}

	public static void saveGraph(String sampledGraphFile, List<String> nodes,
			int[][] sampled) throws IOException {
		File f = new File(sampledGraphFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		String temp;
		for (int i = 0, n = sampled.length; i < n; i++) {
			String top = nodes.get(sampled[i][0]);
			String tail = nodes.get(sampled[i][1]);
			int topInt = Integer.parseInt(top);
			int tailInt = Integer.parseInt(tail);
			if (topInt > tailInt) {
				temp = top;
				top = tail;
				tail = temp;
			}
			bw.write(top + " " + tail + "\r\n");

		}
		bw.flush();
		bw.close();
	}

	public static void mkdirs(String filename) {
		File dir = new File(filename);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void saveList(String filename, Edge[] similars)
			throws IOException {
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (Edge s : similars) {
			bw.write(s.toString() + "\r\n");
		}
		bw.flush();
		bw.close();
	}

	public static BufferedWriter saveResInit(String filename, String feature,
			int[] ks) throws IOException {
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		bw.write(sdf.format(new Date()) + "\r\n");
		bw.write(feature + "\r\nfold\t");
		for (int s : ks) {
			bw.write(s + "\t");
		}
		return bw;
	}

	public static void saveRes(BufferedWriter bw, String info, double[] precision)
			throws IOException {
		bw.write(info+"\t");
		for (double s : precision) {
			bw.write(df.format(s) + "\t");
		}
		bw.write("\r\n");
	}

	public static BufferedWriter saveResInit(String filename, String feature) throws IOException {
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		bw.write(sdf.format(new Date()) + "\r\n");
		bw.write(feature + "\r\n");
		return bw;
	}
	
	public static void saveRes(BufferedWriter bw, long res) throws IOException {
		bw.write(df.format(res) + "\t");
	}
	
	public static void saveResClose(BufferedWriter bw) throws IOException {
		bw.flush();
		bw.close();
	}


}
