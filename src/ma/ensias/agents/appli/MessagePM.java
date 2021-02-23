package ma.ensias.agents.appli;


import jade.core.Agent;


import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MessagePM extends SimpleBehaviour {
	boolean finished = false;
	
	public MessagePM(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		//MessageTemplate msgTemp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		MessageTemplate msgTemp = MessageTemplate.MatchConversationId("decontamination");
		ACLMessage msgRec = myAgent.receive(msgTemp);
		if(msgRec != null) {
			System.out.println("--->" + myAgent.getLocalName() + " a reçu un message de "+msgRec.getSender().getLocalName() + " : "+msgRec.getContent());
			if(msgRec.getContent().contains("decontamine")) {
				this.finished = true;
			}
			else {
				Node node = Arbre.getNode(msgRec.getContent());
				myAgent.addBehaviour(new WalkBehaviour(myAgent, node));
			}
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		return finished;
	}

}
