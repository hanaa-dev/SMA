package ma.ensias.agents.appli;

import jade.core.Agent;

import jade.core.ContainerID;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.ControllerException;

public class Virus  extends Agent{
	private static final long serialVersionUID = 4456792220215457044L;

	protected void setup() { 
		System.out.println("Agent " + getLocalName() + " : cree : ");
		
		addBehaviour(new TickerBehaviour(this, 6000) {
			private static final long serialVersionUID = 9110686473074162535L;

			@Override
			protected void onTick() {
				String nodeName = Arbre.getRandomNode();
				System.out.println("destination prochaine (virus) : "+nodeName);
				ContainerID destination = new ContainerID();destination.setName(nodeName);
				doMove(destination);
			}
		});
		
	}
	
	protected void takeDown() {
		System.out.println("Agent detruit : "+getLocalName());
	}

	protected void beforeMove(){
		try{
			System.out.println("Agent (" +getLocalName()+") part de : "
				+getContainerController().getContainerName());
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	protected void afterMove(){
		try{
			String nodeName = getContainerController().getContainerName();
			System.out.println("Agent (" +getLocalName()+") arrive à : "+nodeName);
			
			if(!nodeName.equals("ROOT")) {
				Arbre.contaminerNode(nodeName);
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
}
