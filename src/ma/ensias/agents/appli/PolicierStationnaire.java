package ma.ensias.agents.appli;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import ma.ensias.agents.env.Plateforme;

public class PolicierStationnaire extends Agent{
	private static final long serialVersionUID = 91209989564106466L;

	protected void setup() { 
		System.out.println("Agent " + getLocalName() + " : cree : ");
		
		addBehaviour(new TickerBehaviour(this, 500) {
			private static final long serialVersionUID = -6026715500028774337L;

			@Override
			protected void onTick() {
				if(Arbre.virusDetruit && Arbre.ReseauDecontamine()) {
					System.out.println("\n-----------------------------------------------------------------------------------------------");
					System.out.println("\t\t***** Le virus est detruit + Le reseau est decontamine *****");
					System.out.println("-----------------------------------------------------------------------------------------------\n");
					doDelete();
				}
				
			}
		});
		
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = -2842739098641914143L;

			@Override
			public void action() {
				ContainerID containerRoot= new ContainerID();containerRoot.setName("ROOT");
				
				if(!Arbre.virusDetruit) {
					myAgent.addBehaviour(new CaptureVirus(myAgent, "ROOT"));
				}
				
				boolean isContamine = Arbre.isNodeContamine("ROOT");
				if(isContamine) {
					Arbre.decontaminerNode("ROOT");
					System.out.println("[****]>"+myAgent.getLocalName()+" a decontamine son noeud [ROOT]");
				}
			}
		});
		
		try {
			Thread.sleep(10000);
			Node node = Arbre.getNode(Arbre.getRandomNode());
			AgentController ac = Plateforme.containers.get("ROOT").createNewAgent("PolicierM", PolicierMobile.class.getName(), new Object []{node});
			ac.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		addBehaviour(new MessagePS(this));
		
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
			System.out.println("Agent (" +getLocalName()+") arrive à : "
					+getContainerController().getContainerName());
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
}
