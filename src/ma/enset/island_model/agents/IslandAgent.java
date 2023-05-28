package ma.enset.island_model.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.island_model.helpers.GAUtils;
import ma.enset.island_model.entites.Individual;

import java.util.*;

public class IslandAgent extends Agent {

    @Override
    protected void setup() {
        initialaizePopulation();
        calculateIndFintess();
        sortPopulation();
        addBehaviour(new Behaviour() {
            int it = 0;
            @Override
            public void action() {
                selection();
                crossover();
                mutation();
                calculateIndFintess();
                sortPopulation();
                it++;
            }

            @Override
            public boolean done() {
                if(it ==GAUtils.MAX_IT || getBest().getFitness()==GAUtils.MAX_FITNESS){
                    sendBest();
                    return true;
                }
                return false;
            }
        });


    }


    //methods needed for applying genetic algorithm

    List<Individual> individuals=new ArrayList<>();
    Individual firstFitness;
    Individual secondFitness;
    Random rnd=new Random();
    public void initialaizePopulation(){
        for (int i = 0; i< GAUtils.POPULATION_SIZE; i++){
            individuals.add(new Individual());
        }
    }
    public void calculateIndFintess(){
        for (int i=0;i<GAUtils.POPULATION_SIZE;i++){
            individuals.get(i).calculateFitness();
        }

    }
    public void selection(){
        firstFitness=individuals.get(0);
        secondFitness=individuals.get(1);
    }

    public void crossover(){

        int pointCroisment=rnd.nextInt(5);
        pointCroisment++;

        Individual individual1=new Individual();
        Individual individual2=new Individual();
        for (int i=0;i<individual1.getGenes().length;i++) {
            individual1.getGenes()[i]=firstFitness.getGenes()[i];
            individual2.getGenes()[i]=secondFitness.getGenes()[i];
        }
        for (int i=0;i<pointCroisment;i++) {
            individual1.getGenes()[i]=secondFitness.getGenes()[i];
            individual2.getGenes()[i]=firstFitness.getGenes()[i];
        }

        individuals.set(individuals.size()-2,individual1);
        individuals.set(individuals.size()-1,individual2);
    }
    public void mutation(){
        int index=rnd.nextInt(GAUtils.CHROMOSOME_SIZE);
        if (rnd.nextDouble()<GAUtils.MUTATION_PROBABILITY){
            individuals.get(individuals.size()-2).getGenes()[index]=GAUtils.CHARACTERS.charAt(rnd.nextInt(GAUtils.CHARACTERS.length()));
        }
        index=rnd.nextInt(GAUtils.CHROMOSOME_SIZE);
        if (rnd.nextDouble()<GAUtils.MUTATION_PROBABILITY){
            individuals.get(individuals.size()-1).getGenes()[index]=GAUtils.CHARACTERS.charAt(rnd.nextInt(GAUtils.CHARACTERS.length()));
        }


    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
    public void sortPopulation(){
        Collections.sort(individuals,Collections.reverseOrder());
    }
    public Individual getBest(){
        return individuals.get(0);
    }

    public void sendBest(){
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] masterDescription = DFService.search(this, dfAgentDescription);
            ACLMessage aclMessage = new ACLMessage();
            aclMessage.addReceiver(masterDescription[0].getName());
            aclMessage.setContent(Arrays.toString(getBest().getGenes()) + "-" + getBest().getFitness());
            send(aclMessage);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }
}