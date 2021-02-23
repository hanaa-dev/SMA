package ma.ensias.agents.appli;

import jade.core.Agent;


import jade.core.ContainerID;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class WalkBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 7046093065330398580L;
	
	private Node node;
	ContainerID destination = new ContainerID();
	static NodeIterator iter;
	boolean finished = false;
	
	public WalkBehaviour(Agent a,Node n) {
		super(a,3000);
		this.node = n;
		destination = new ContainerID();
		this.iter = new NodeIterator(n);
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		String nodeName;
		if(iter.hasNext()) {
			this.node = iter.next();
			nodeName = node.getName();
			System.out.println("destination prochaine (PolicierM) : "+nodeName);
			destination.setName(nodeName);
			myAgent.doMove(destination);
			
		}
		else {
			destination.setName("ROOT");
			myAgent.doMove(destination);
			stop();
		}
	}

}
