package ma.enset.sma;

import jade.core.AID;
import ma.enset.sequential.entites.Individual;

public class FitnessAgent implements Comparable{
    private AID aid;
    private int fitness;

    public FitnessAgent(AID aid, int fitness) {
        this.aid = aid;
        this.fitness = fitness;
    }

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Object o) {
        FitnessAgent fitnessAgent = (FitnessAgent)  o;
        if (this.getFitness() < ((FitnessAgent ) o).getFitness()) return -1;
        else if (this.getFitness() > ((FitnessAgent ) o).getFitness()) return 1;
        return 0;
    }
}
