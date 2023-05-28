package ma.enset.island_model.entites;

import jade.core.AID;
import ma.enset.island_model.helpers.GAUtils;

import java.util.Random;

public class Individual implements Comparable{
    //Bonjour
    //chromosome
    private char genes[]=new char[GAUtils.CHROMOSOME_SIZE];
    private int fitness;
    private String agentName;

    public String  getAgentName() {
        return agentName;
    }

    public void setAgentName(String  agentName) {
        this.agentName = agentName;
    }

    public Individual(char[] genes, int fitness, String  agentName) {
        this.agentName=agentName;
        this.genes = genes;
        this.fitness = fitness;
    }

    public Individual() {
        Random rnd=new Random();
        for (int i=0;i<genes.length;i++){
            genes[i]= GAUtils.CHARACTERS.charAt(rnd.nextInt(GAUtils.CHARACTERS.length()));
        }
    }
    public void calculateFitness(){
        fitness=0;
        for (int i=0;i<GAUtils.CHROMOSOME_SIZE;i++) {
            if(genes[i]==GAUtils.SOLUTION.charAt(i))
                fitness+=1;
        }
    }

    public char[] getGenes() {
        return genes;
    }

    public void setGenes(char[] genes) {
        this.genes = genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual=(Individual) o;
        if (this.fitness>individual.fitness)
            return 1;
        else if(this.fitness<individual.fitness){
            return -1;
        }else
            return 0;
    }
}