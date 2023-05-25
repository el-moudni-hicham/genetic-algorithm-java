package ma.enset.sma;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.sequential.entites.Individual;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralAgent extends Agent {
    List<FitnessAgent> agentsFitness = new ArrayList<>();

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
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
            @Override
            public void action() {
                ACLMessage receiveMsg = receive();
                if(receiveMsg != null){
                    int fitness = Integer.parseInt(receiveMsg.getContent());
                    AID sender = receiveMsg.getSender();
                    setAgentsFitness(sender, fitness);
                }
            }
        });
    }
    private void calculateFitness(){
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        for (FitnessAgent fa: agentsFitness) {
            message.addReceiver(fa.getAid());
        }
        message.setContent("fitness");
        send(message);
    }

    private void setAgentsFitness(AID aid, int fitness){
        for (int i = 0; i < agentsFitness.size(); i++) {
            if(agentsFitness.get(i).getAid().equals(aid)){
                agentsFitness.get(i).setFitness(fitness);
            }
        }
    }
}
