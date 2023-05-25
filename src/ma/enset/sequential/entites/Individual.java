package ma.enset.sequential.entites;

import ma.enset.sequential.helpers.GAUtils;

import java.util.Random;

public class Individual implements Comparable{
    // chromosome
    private char genes[] = new char[GAUtils.CHROMOSOME_SIZE];
    private int fitness;
    Random rnd = new Random();
    public Individual() {
        for (int i=0 ; i < genes.length ; i++){
            int pos = rnd.nextInt(GAUtils.CHARACTERS.length());
            genes[i] = GAUtils.CHARACTERS.charAt(pos);
        }
    }

    public void calculateFitness(){
        fitness = 0;
        for (int i = 0; i < GAUtils.CHROMOSOME_SIZE; i++) {
            if (genes[i] == GAUtils.SOLUTION.charAt(i))
                fitness+=1;
        }

    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public char[] getGenes() {
        return genes;
    }

    public void setGenes(char[] genes) {
        this.genes = genes;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual = (Individual) o;
        if (this.getFitness() < ((Individual) o).getFitness()) return -1;
        else if (this.getFitness() > ((Individual) o).getFitness()) return 1;
        return 0;
    }
}
