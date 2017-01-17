package whu.cs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import whu.cs.pojo.Edge;

public class Utils {

	public static boolean isConnected(Map<Integer, List<Integer>> edges) {
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>(
				edges.size());
		for (Integer node : edges.keySet()) {
			visited.put(node, false);
		}
		Iterator<Entry<Integer, List<Integer>>> it = edges.entrySet()
				.iterator();
		dfs(it.next().getKey(), edges, visited);
		for (Entry<Integer, Boolean> id : visited.entrySet()) {
			if (!id.getValue())
				return false;
		}
		return true;
	}

	private static void dfs(Integer node, Map<Integer, List<Integer>> edges,
			Map<Integer, Boolean> visited) {
		visited.put(node, true);
		List<Integer> adjs = edges.get(node);
		if (adjs == null || adjs.size() == 0)
			return;
		for (int adj : adjs) {
			if (!visited.get(adj)) {
				dfs(adj, edges, visited);
			}
		}

	}

	public static void removeEdge(Map<Integer, List<Integer>> edges,
			Integer top, Integer tail, boolean isDirected) {
		boolean flag = true;
		List<Integer> adjs = edges.get(top);
		flag = adjs.remove(tail);
		if (!flag) {
			System.out.println("can not find edge1 : " + top + ":" + tail);
		}
		if (!isDirected) {
			adjs = edges.get(tail);
			flag = adjs.remove(top);
			if (!flag) {
				System.out.println("can not find edge2 : " + tail + ":" + top);
			}
		}
	}

	public static void addEdge(Map<Integer, List<Integer>> edges, Integer top,
			Integer tail) {
		List<Integer> adjs = edges.get(top);
		adjs.add(tail);
		adjs = edges.get(tail);
		adjs.add(top);
	}

	public static Map<Integer, List<Integer>> copyMap(
			Map<Integer, List<Integer>> src) {
		Map<Integer, List<Integer>> copy = new HashMap<Integer, List<Integer>>();
		for (Entry<Integer, List<Integer>> entry : src.entrySet()) {
			int key = entry.getKey();
			List<Integer> copy_value = new ArrayList<Integer>();
			for (int value : entry.getValue()) {
				copy_value.add(value);
			}
			copy.put(key, copy_value);
		}
		return copy;
	}

	public static double cosineSimilarity(double[] vec1, double[] vec2) {
		double dotProduct = dotProduct(vec1, vec2);
		double normVec1 = Math.sqrt(dotProduct(vec1, vec1));
		double normVec2 = Math.sqrt(dotProduct(vec2, vec2));
		return Math.abs(dotProduct / (normVec1 * normVec2));
	}

	private static double dotProduct(double[] vec1, double[] vec2) {
		double dotProduct = 0.0;
		for (int i = 0, size = vec1.length; i < size; i++) {
			dotProduct += vec1[i] * vec2[i];
		}
		return dotProduct;
	}

	public static List<String> sortMapByValue(Map<String, Double> map,
			int threshold) {
		List<String> list = new ArrayList<String>(threshold);
		List<Entry<String, Double>> list_tmp = new ArrayList<Entry<String, Double>>(
				map.entrySet());
		Collections.sort(list_tmp, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> arg0,
					Entry<String, Double> arg1) {
				double r = arg1.getValue() - arg0.getValue();
				if (r > 0)
					return 1;
				else if (r < 0)
					return -1;
				else
					return 0;
			}
		});
		int i = 0;
		for (Entry<String, Double> entry : list_tmp) {
			String item = entry.getKey() + "\t" + entry.getValue();
			list.add(item);
			if (++i == threshold)
				break;
		}
		return list;
	}

	public static void heapAdjust(Edge[] similars, int idx) {
		int size = similars.length;
		int left = (idx << 1) + 1;
		int right = (idx << 1) + 2;
		int min = idx;
		if (left < size && similars[min].similarity > similars[left].similarity) {
			min = left;
		}
		if (right < size
				&& similars[min].similarity > similars[right].similarity) {
			min = right;
		}
		if (min == idx) {
			return;
		} else {
			double tmp1 = similars[idx].similarity;
			similars[idx].similarity = similars[min].similarity;
			similars[min].similarity = tmp1;
			int tmp2 = similars[idx].id1;
			similars[idx].id1 = similars[min].id1;
			similars[min].id1 = tmp2;
			tmp2 = similars[idx].id2;
			similars[idx].id2 = similars[min].id2;
			similars[min].id2 = tmp2;
			heapAdjust(similars, min);
		}
	}
}
