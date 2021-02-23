package ma.ensias.agents.env;
import java.util.HashMap;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.ContainerController;

public class Plateforme {
	private ContainerController mainContainer;
	Runtime rt;
	
	public static HashMap<String,ContainerController> containers = new HashMap<String, ContainerController>();
	
	public Plateforme() {
		rt =  Runtime.instance(); 
		
		Properties p = new ExtendedProperties();
		p.setProperty("gui", "true");
		
		ProfileImpl profile = new ProfileImpl(p);
		mainContainer = rt.createMainContainer(profile);
	}
	
	public ContainerController getMainContainer(){ 
		return mainContainer;
	}
	
	//Créer un nouveau container
	public void createAgentContainer(String containerName) {
		ProfileImpl profile = new ProfileImpl(false);
		profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		profile.setParameter(ProfileImpl.CONTAINER_NAME, containerName);
		ContainerController cc = rt.createAgentContainer(profile);
		containers.put(containerName, cc);
	}
}
