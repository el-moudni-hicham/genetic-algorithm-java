package ma.enset.sma.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import ma.enset.sequential.helpers.GAUtils;

import java.util.Random;


public class IndividualAgent extends Agent {
    // chromosome
    private char genes[] = new char[GAUtils.CHROMOSOME_SIZE];
    private int fitness;
    Random random = new Random();

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("ga");
        serviceDescription.setName("ga_ma");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        for (int i=0 ; i < genes.length ; i++){
            int pos = random.nextInt(GAUtils.CHARACTERS.length());
            genes[i] = GAUtils.CHARACTERS.charAt(pos);
        }
        // Mutation Operation
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMsg = receive();
                if(receivedMsg != null){
                    switch (receivedMsg.getContent()){
                        case "mutation" : mutation(); break;
                        case "fitness"  : calculateFitness(receivedMsg); break;
                        case "chromosome" : sendChromosome(receivedMsg); break;
                        case "change chromosome" : changeChromosome(receivedMsg); break;
                    }
                }else {
                    block();
                }

            }
        });
    }

    private void mutation(){
        int index = random.nextInt(GAUtils.CHROMOSOME_SIZE);
        if (random.nextDouble() < GAUtils.MUTATION_PROBABILITY) {
            genes[index] = GAUtils.CHARACTERS.charAt(random.nextInt(GAUtils.CHARACTERS.length()));
        }
    }
    private void calculateFitness(ACLMessage receivedMsg){
        fitness = 0;
        for (int i = 0; i < GAUtils.CHROMOSOME_SIZE; i++) {
            if (genes[i] == GAUtils.SOLUTION.charAt(i))
                fitness+=1;
        }
        ACLMessage reply = receivedMsg.createReply();
        reply.setContent(String.valueOf(fitness));
        send(reply);
    }

    private void sendChromosome(ACLMessage receivedMsg) {
        ACLMessage reply = receivedMsg.createReply();
        reply.setContent(new String(genes));
        send(reply);
    }

    private void changeChromosome(ACLMessage receivedMsg) {
        genes = receivedMsg.getContent().toCharArray();
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }
}
