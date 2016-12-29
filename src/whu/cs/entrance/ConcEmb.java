package whu.cs.entrance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import whu.cs.util.FileUtils;

public class ConcEmb {

	public static void main(String args[]) throws IOException {
		String curPath = args[0];
		int vecNum = Integer.parseInt(args[1]);
		String[] inputs = new String[vecNum];
		for (int i = 0; i < vecNum; i++) {
			inputs[i] = args[i];
		}
		String output = args[2 + vecNum];
		
		int start = 0;
		Map<String, StringBuilder> feaMap = new HashMap<String, StringBuilder>();
		for (int i = 0; i < vecNum; i++) {
			start += concNextInput(feaMap, curPath + "/" + inputs[i], start);
		}
		FileUtils.writeMap(curPath + "/" + output, feaMap, start);
		System.out
				.println("------------------ConcEmbedding done-------------------------");

	}

	private static int concNextInput(Map<String, StringBuilder> feaMap,
			String input, int start) throws IOException {
		File input_file = new File(input);
		BufferedReader br = new BufferedReader(new FileReader(input_file));
		String line = null;
		StringBuilder vector = new StringBuilder();
		for(int i=0;i<start;i++){vector.append("0.0 ");}
		@SuppressWarnings("null")
		int size = Integer.parseInt(line.split("\\s+")[1]);
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+",2);
			String key = items[0];
			StringBuilder value = null;
			if (feaMap.containsKey(key)) {
				value = feaMap.get(key);
			} else {
				value = new StringBuilder();
				value.append(vector);//????
				feaMap.put(key, value);
			}
			value.append(items[1]);
		}
		br.close();
		return size;
	}

}