package ma.enset.sma.containers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ma.enset.sma.helpers.GAUtils;

public class CentralContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);

        AgentController mainAgent = agentContainer.createNewAgent("mainAgent", ma.enset.sma.agents.CentralAgent.class.getName(), new Object[]{});
        mainAgent.start();

    }
}
