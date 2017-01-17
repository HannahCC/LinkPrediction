package whu.cs.pojo;

public class Edge implements Comparable<Edge> {
	public int id1;
	public int id2;
	public double similarity;


	public Edge() {
		// TODO Auto-generated constructor stub
	}
	
	public Edge(int id1, int id2, double similarity) {
		this.id1 = id1;
		this.id2 = id2;
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

	
	public String transfer() {
		String edge = id1 < id2 ? id1 + " " + id2 : id2 + " " + id1;
		return edge + "\t" + similarity;
	}
	
	@Override
	public String toString() {
		return id1 + " " + id2 + "\t" + similarity;
	}

	public String traverse() {
		return id2 + " " + id1 + "\t" + similarity;
	}
}
