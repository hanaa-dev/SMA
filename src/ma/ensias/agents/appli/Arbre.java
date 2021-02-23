package ma.ensias.agents.appli;

import java.util.ArrayList;



import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

import jade.content.OntoAID;

public class Arbre {
	static Node root;
	
	static Graph graph;
	static Viewer view;
	static String currentNodeName = ""; 
	static boolean virusDetruit = false;
	
	protected String styleSheet =
            "node { "
                 + "size: 20px;"
                 + "fill-color: green;"
                 + "text-alignment: under;"
                 + "text-background-mode: plain;"
                 + "text-background-color: white;"
            + "} "
            + "node.virus { "
                 + "size: 20px; "
                 + "fill-color: red; "
            + "}";
	
	public Arbre() {
		this.initTree();
		System.setProperty("org.graphstream.ui", "swing");
		graph = new MultiGraph("Arbre");
		graph.setAttribute("ui.stylesheet", styleSheet);
		this.remplireGraph(root);
		this.printLabels();
		view = graph.display();
	}
	
	public void initTree() {
		
		root = new Node("ROOT");
		Node n1 = new Node("N1");
		Node n2 = new Node("N2");
		Node n3 = new Node("N3");
		Node n4 = new Node("N4");
		Node n5 = new Node("N5");
		Node n6 = new Node("N6");
		Node n7 = new Node("N7");
		n1.addChild(n3);n1.addChild(n4);
		n2.addChild(n5);n2.addChild(n6);
		n6.addChild(n7);
		root.addChild(n1);root.addChild(n2);
	}
	
	
	public static Node getNode(String nodeName) {
		for(Node node: root) {
			if(node.getName().equals(nodeName))
				return node;
		}
		return null;
	}
	
	
	public Graph getGraph() {
		return graph;
	}
	
	
	public static String getRandomNode() {
		List<String> list = getAllNodeNames();
		Random r = new Random();
		
		String randNode = list.get(r.nextInt(list.size()));
		
		return (!randNode.equals(currentNodeName)) ? randNode : getRandomNode();
	}
	
	
	public static synchronized void updateView() {
		view.replayGraph(graph);
		printTreeInConsole();
	}
	
	
	public static boolean agentExistInContainer(jade.util.leap.List containerAgents, String agentName) {
		Iterator it = containerAgents.iterator();
		while (it.hasNext()) {
			OntoAID oa = (OntoAID) it.next();
			if (oa.getName().contains(agentName)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String getPremierNodeContamine() {
		for(Node node: root) {
			if(!node.isRoot() && node.isEstContamine())
				return node.getName();
		}
		return null;
	}
	
	
	public static boolean isNodeContamine(String nodeName) {
		for(Node node : root) {
			if(node.getName().equals(nodeName)) {
				if(node.isEstContamine()) {
					return true;
				}
				else break;
			}
		}
		return false;
	}
	
	
	public static synchronized void contaminerNode(String name) {
		for(Node node : root) {
			if(node.getName().equals(name)) {
				node.setEstContamine(true);
				styleVirus(name);
				if(!node.isRoot())	{
					node.getParent().setEstContamine(true);
					styleVirus(node.getParent().getName());
				}
				for(Node c: node.getFils()) {
					c.setEstContamine(true);
					styleVirus(c.getName());
				}
				updateView();
				break;
			}
		}
		
	}
	
	
	public static synchronized void decontaminerNode(String name) {
		for(Node node : root) {
			if(node.getName().equals(name)) {
				node.setEstContamine(false);
				graph.getNode(name).removeAttribute("ui.class");
				updateView();
				break;
			}
		}
	}
	
	
	public static Node getNext(Node n) {
		Node node = null;
		Iterator<Node> nodes = n.iterator();
		while(nodes.hasNext()) {
			
		}
		
		return node;
	}
	
	
	public static boolean ReseauDecontamine() {
		for(Node n : root) {
			if(n.isEstContamine()) return false;
		}
		return true;
	}

	
	public static List<String> getAllNodeNames() {
		ArrayList<String> nodes = new ArrayList<>();
		for (Node node : root) {
			nodes.add(node.getName());
		}
		return nodes;
	}
	
	
	public static void printTreeInConsole() {
		System.out.println("\n-----------------------------------------------------------");
		System.out.println("|\t\t Etat actuel du Reseau");
		System.out.println("-----------------------------------------------------------");
		for (Node node : root) {
			String indent = createIndent(node.getLevel());
			System.out.println("|" +indent + node.toString());
		}
		System.out.println("-----------------------------------------------------------\n");
	}
	
	
	public void remplireGraph(Node n) {
		String n1 = n.getName();
		if(n.isRoot()) graph.addNode(n1);
		if(n.estContamine) styleVirus(n1);
		
		if(!n.getFils().isEmpty()) {
			for(Node c : n.getFils()) {
				String n2 = c.getName();
				graph.addNode(n2);
				graph.addEdge(n1+"_"+n2, n1, n2);
				if(c.estContamine) styleVirus(n2);
				this.remplireGraph(c);
			}
		}
	}
	
	
	public void printLabels() {
		for (org.graphstream.graph.Node node : graph) {
	        node.setAttribute("ui.label", node.getId());
	    }
	}
	
	
	public static void styleVirus(String n) {
		graph.getNode(n).setAttribute("ui.class", "virus");
	}
	
	
	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
