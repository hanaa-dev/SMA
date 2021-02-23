package ma.ensias.agents.appli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node implements Iterable<Node>, Serializable {
	private static final long serialVersionUID = 1631896225598850203L;
	
	String name;
	boolean estContamine;
	Node parent;
	List<Node> fils;
	
	public Node() {}
	
	public Node(String name) {
		this.name = name;
		this.estContamine = false;
		this.fils = new ArrayList<Node>();
	}
	
	public Node addChild(Node child) {
		child.parent = this;
		this.fils.add(child);
		return child;
	}
	
	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}

	@Override
	public String toString() {
		return "Node [" + name + ", estContamine=" + estContamine + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEstContamine() {
		return estContamine;
	}

	public void setEstContamine(boolean estContamine) {
		this.estContamine = estContamine;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getFils() {
		return fils;
	}

	public void setFils(List<Node> fils) {
		this.fils = fils;
	}

	@Override
	public Iterator<Node> iterator() {
		NodeIterator iter = new NodeIterator(this);
		return iter;
	}
	
	//public void displayGraph(Node n) {
		
	//}
	
	
}
