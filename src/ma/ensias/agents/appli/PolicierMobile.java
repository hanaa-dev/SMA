package ma.ensias.agents.appli;

import jade.content.lang.sl.SLCodec;



import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.ActionExecutor;
import jade.core.behaviours.OutcomeManager;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.QueryAgentsOnLocation;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import ma.ensias.agents.env.Plateforme;

public class PolicierMobile extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3231363022593305085L;
	private Node node;
	protected void setup() { 
		System.out.println("Agent " + getLocalName() + " : cree : ");
		
		getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
		
		Object[] args= getArguments();
		node =(Node)args[0];
		
		addBehaviour(new WalkBehaviour(this, node));
		
		addBehaviour(new MessagePM(this));
		
		addBehaviour(new TickerBehaviour(this, 500) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				if(Arbre.virusDetruit && Arbre.ReseauDecontamine()) {
					doDelete();
				}
				
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
			System.out.println("Agent (" +getLocalName()+") arrive à : " + nodeName);
			
			if(nodeName.equals("ROOT")) {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				//msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
				msg.setConversationId("decontamination");
				
				msg.setSender(getAID());
				msg.addReceiver(new AID("PolicierS", AID.ISLOCALNAME));
				msg.setContent("j'ai termine la decontamination de la branche que vous m'avez envoye");
				
				send(msg);
			} else {
				if(!Arbre.virusDetruit) {
					addBehaviour(new CaptureVirus(this, nodeName));
				}
				if(Arbre.isNodeContamine(nodeName)) {
					Arbre.decontaminerNode(nodeName);
					System.out.println("[****]>"+getLocalName()+" a decontamine le noeud : "+nodeName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
