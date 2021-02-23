package ma.ensias.agents.appli;

import jade.core.Agent;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ma.ensias.agents.env.Plateforme;

public class MessagePS extends SimpleBehaviour {
	
	boolean finished = false;
	
	public MessagePS(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		MessageTemplate msgTemp = MessageTemplate.MatchConversationId("decontamination");
		ACLMessage msgRec = myAgent.receive(msgTemp);
		if(msgRec != null) {
			System.out.println("--->" + myAgent.getLocalName() + " a reçu un message de "+msgRec.getSender().getLocalName() + " : "+msgRec.getContent());
			if(msgRec.getContent().contains("termine")) {
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setConversationId("decontamination");
				msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
				
				msg.setSender(myAgent.getAID());
				msg.addReceiver(msgRec.getSender());
				
				String nodeName = Arbre.getPremierNodeContamine();
				
				if((Arbre.ReseauDecontamine() && Arbre.virusDetruit) || nodeName == null) {
					msg.setContent("le reseau est decontamine");
					myAgent.send(msg);
					try {
						Thread.sleep(3000);
						Plateforme.containers.get("ROOT").getAgent("PolicierM").kill();
						finished = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					msg.setContent(nodeName);
					myAgent.send(msg);
				}
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
