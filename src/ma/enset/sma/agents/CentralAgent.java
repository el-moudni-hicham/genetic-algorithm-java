package ma.enset.sma.agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
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

    Random random = new Random();
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

        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int counter = 0;
            @Override
            public void action() {
                ACLMessage receiveMsg = receive();
                if(receiveMsg != null){
                    counter++;
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

            @Override
            public boolean done() {
                return counter == GAUtils.POPULATION_SIZE;
            }
        });

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int counter = 0;
            FitnessAgent a1;
            FitnessAgent a2;
            @Override
            public void action() {
                selection();
                crossover();

                Collections.sort(agentsFitness,Collections.reverseOrder());
                sendMessage(agentsFitness.get(0).getAid(),"chromosome",ACLMessage.REQUEST);
                ACLMessage aclMessage=blockingReceive();
                System.out.println("sub beh 4");

                System.out.println(aclMessage.getContent()+" : "+agentsFitness.get(0).getFitness());
                counter++;
            }

            // Selection Operation to get 2 agents with the height fitness value and contact them
            private void selection(){
                System.out.println("--------------- Selection Operation ---------------");
                a1 = agentsFitness.get(0);
                a2 = agentsFitness.get(1);

                sendMessage(a1.getAid(),"chromosome",ACLMessage.REQUEST);
                sendMessage(a2.getAid(),"chromosome",ACLMessage.REQUEST);
            }
            private void crossover(){
                System.out.println("--------------- Crossover Operation ---------------");

                ACLMessage aclMessage1 = blockingReceive();
                ACLMessage aclMessage2 = blockingReceive();

                int crossoverPoint = random.nextInt(GAUtils.CHROMOSOME_SIZE - 2);
                crossoverPoint++;

                System.out.println("Crossover Point : "+ crossoverPoint);

                char [] chromParent1 = aclMessage1.getContent().toCharArray();
                char [] chromParent2 = aclMessage2.getContent().toCharArray();

                char [] chromOffsring1 = new char[GAUtils.CHROMOSOME_SIZE];
                char [] chromOffsring2 = new char[GAUtils.CHROMOSOME_SIZE];


                for (int i = 0; i < chromParent1.length; i++) {
                    chromOffsring1[i] = chromParent1[i];
                    chromOffsring2[i] = chromParent2[i];
                }

                for (int i = 0; i < crossoverPoint; i++) {
                    chromOffsring1[i] = chromParent2[i];
                    chromOffsring2[i] = chromParent1[i];
                }

                /*System.out.println("********  Before Crossover *****");
                System.out.println(chromParent1);
                System.out.println(chromParent2);

                System.out.println("********  After Crossover *****");
                System.out.println(chromOffsring1);
                System.out.println(chromOffsring2);*/

                int fitnessChromo1=0;
                int fitnessChromo2=0;
                for(int i=0;i<GAUtils.CHROMOSOME_SIZE;i++){
                    if(chromOffsring1[i]==GAUtils.SOLUTION.charAt(i)) fitnessChromo1++;
                    if(chromOffsring2[i]==GAUtils.SOLUTION.charAt(i)) fitnessChromo2++;
                }
                agentsFitness.get(GAUtils.POPULATION_SIZE-2).setFitness(fitnessChromo1);
                agentsFitness.get(GAUtils.POPULATION_SIZE-1).setFitness(fitnessChromo2);


                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-2).getAid(),new String(chromOffsring1),
                        ACLMessage.REQUEST);
                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-1).getAid(),new String(chromOffsring2),
                        ACLMessage.REQUEST);

                ACLMessage receivedMsg1=blockingReceive();
                ACLMessage receivedMsg2=blockingReceive();

                setAgentsFitness(receivedMsg1.getSender(),Integer.parseInt(receivedMsg1.getContent()));
                setAgentsFitness(receivedMsg2.getSender(),Integer.parseInt(receivedMsg2.getContent()));

            }

            @Override
            public boolean done() {
                if(counter == GAUtils.MAX_IT || agentsFitness.get(0).getFitness()==GAUtils.MAX_FITNESS){
                    System.out.println();
                    System.out.println("LAST ITERATION " + counter);
                    return true;
                }
                return false;
            }
        });

        addBehaviour(sequentialBehaviour);
    }

    // Send a message to all agents to calculate its fitness
    void calculateFitness(){
        System.out.println("Calculate Fitness");
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

    private void sendMessage(AID aid,String content,int performative){
        ACLMessage aclMessage=new ACLMessage(performative);
        aclMessage.setContent(content);
        aclMessage.addReceiver(aid);
        send(aclMessage);
    }

}

