package ma.ensias.agents.appli;

import jade.core.Agent;

import jade.core.ContainerID;
import jade.core.behaviours.ActionExecutor;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.OutcomeManager;
import jade.domain.JADEAgentManagement.QueryAgentsOnLocation;
import jade.domain.mobility.MobilityOntology;
import ma.ensias.agents.env.Plateforme;

public class CaptureVirus extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	ContainerID location;
	
	public CaptureVirus(Agent a,String nodeName) {
		super(a);
		this.location = new ContainerID(); this.location.setName(nodeName);
	}

	@Override
	public void action() {
		QueryAgentsOnLocation qa = new QueryAgentsOnLocation();
		qa.setLocation(location);
		ActionExecutor<QueryAgentsOnLocation, jade.util.leap.List> ae = 
				new ActionExecutor<QueryAgentsOnLocation, jade.util.leap.List>(qa, MobilityOntology.getInstance(), myAgent.getAMS()) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 3683595080565157309L;

					@Override
					public int onEnd() {
						int ret = super.onEnd();
						if (getExitCode() == OutcomeManager.OK) {
							//la liste des agents est bien recuperee
							jade.util.leap.List containerAgents = getResult();
							boolean virusExist = Arbre.agentExistInContainer(containerAgents, "Virus");
							//si un virus existe sur le noeud, l'agent policier va le détruire
							if(virusExist) {
								try {
									Plateforme.containers.get(location.getName()).getAgent("Virus").kill();
									System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Je suis l'agent "+myAgent.getLocalName()+" , j'ai détruit le virus\n");
									Arbre.virusDetruit = true;
								} catch (Exception e) {}
							}
						}
						else {
							System.out.println("Un erreur est survenu lors de la recuperation de la liste des agents. "+getErrorMsg());
						}
						return ret;
					}
				};
		myAgent.addBehaviour(ae);
	}

}
