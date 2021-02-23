package ma.ensias.agents.appli;

import jade.wrapper.AgentController;

import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import ma.ensias.agents.env.Plateforme;

public class Client {
	private ContainerController mainContainer;
	Plateforme plateforme;
	Arbre sim;
	
	public Client() throws StaleProxyException {
		plateforme = new Plateforme();
		mainContainer = plateforme.getMainContainer();
		
		this.sim = new Arbre();
		
		for(String name: this.sim.getAllNodeNames()) {
			plateforme.createAgentContainer(name);
		}
		
		this.sim.printTreeInConsole();
		
		AgentController ac = Plateforme.containers.get("ROOT").createNewAgent("PolicierS", 
				PolicierStationnaire.class.getName(), null);
		
		AgentController ac1 = mainContainer.createNewAgent("Virus", 
				Virus.class.getName(), null);
		
		ac.start();ac1.start();
		
	}
	
	public void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws StaleProxyException {
		new Client();
	}
}
