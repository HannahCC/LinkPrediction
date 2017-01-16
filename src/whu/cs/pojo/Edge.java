package whu.cs.pojo;


public class Edge implements Comparable<Edge>{
	public String edge;
	public double similarity;

	public Edge() {}
	public Edge(int id1, int id2, double similarity) {
		this.edge = id1 < id2 ? id1 + " " + id2 : id2 + " "
				+ id1;
		this.similarity = similarity;
	}

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
