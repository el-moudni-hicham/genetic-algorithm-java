package ma.enset.sma.agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;
import ma.enset.sma.helpers.FitnessAgent;
import ma.enset.sma.helpers.GAUtils;

import java.util.*;

public class CentralAgent extends Agent {
    List<FitnessAgent> agentsFitness = new ArrayList<>();

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("ga");

        dfAgentDescription.addServices(serviceDescription);

        // Return all agents with service type = "ga"
        try {
            DFAgentDescription[] search = DFService.search(this, dfAgentDescription);
            for (DFAgentDescription dfa: search) {
                agentsFitness.add(new FitnessAgent(dfa.getName(), 0));
            }
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        calculateFitness();
        addBehaviour(new CyclicBehaviour() {
            int counter = 0;
            @Override
            public void action() {
                ACLMessage receiveMsg = receive();
                counter++;
                if(receiveMsg != null){
                    int fitness = Integer.parseInt(receiveMsg.getContent());
                    AID sender = receiveMsg.getSender();
                    setAgentsFitness(sender, fitness);

                    if(counter == GAUtils.POPULATION_SIZE){
                        Collections.sort(agentsFitness, Collections.reverseOrder());
                        showPopulation();
                    }
                } else {
                    block();
                }
            }
        });
    }

    // Send a message to all agents to calculate its fitness
    private void calculateFitness(){
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        for (FitnessAgent fa: agentsFitness) {
            message.addReceiver(fa.getAid());
        }
        message.setContent("fitness");
        send(message);
    }

    // Get All fitness and store them to List "agentsFitness"
    private void setAgentsFitness(AID aid, int fitness){
        for (int i = 0; i < agentsFitness.size(); i++) {
            if(agentsFitness.get(i).getAid().equals(aid)){
                agentsFitness.get(i).setFitness(fitness);
            }
        }
    }

    // Show agents informations
    private void showPopulation(){
        for (FitnessAgent fa : agentsFitness) {
            System.out.println(fa.getAid().getName() + " " + fa.getFitness());
        }
    }

}

