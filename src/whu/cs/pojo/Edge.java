package whu.cs.pojo;


public class Edge implements Comparable<Edge>{
	public String edge;
	public double similarity;

	@Override
	public int compareTo(Edge o) {
		if (o.similarity > this.similarity)
			return 1;
		else if (o.similarity < this.similarity)
			return -1;
		return 0;
	}
	
	@Override
	public String toString(){
		return edge+"\t"+similarity;
	}
}
