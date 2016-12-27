package whu.cs.pojo;

import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable<String>{
	String id;
	private Set<Node> adjacents = null;

	public Node(String id) {
		this.id = id;
		this.adjacents = new HashSet<Node>();

	}

	public String getId() {
		return id;
	}

	public void addAdjacent(Node adj) {
		adj.addAdjacent(adj);

	}

	public Set<Node> getAdjacents() {
		return adjacents;
	}

	@Override
	public int compareTo(String o) {
		return this.id.compareTo(o);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

}
