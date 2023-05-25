package ma.enset.sequential.application;

import ma.enset.sequential.entites.Population;
import ma.enset.sequential.helpers.GAUtils;

import java.util.Arrays;

public class GA {
    public static void main(String[] args) {
        Population population = new Population();
        population.initializePopulation();
        population.calculateIndividualFitnessValue();
        population.sortPoplation();

        population.getIndividuals().forEach(i -> {
            System.out.println("Chromosome : " + Arrays.toString(i.getGenes()) + " --> Fitness Value : " + i.getFitness());
        });

        System.out.println("--------- After Selection and Crossover and Mutation Operations ---------");

        int it = 0;
        while (it< GAUtils.MAX_IT && population.getBestIndividual().getFitness()<GAUtils.CHROMOSOME_SIZE) {
            population.selection();
            population.crossover();


            population.mutation();

            population.calculateIndividualFitnessValue();
            population.sortPoplation();
            System.out.println("It "+ it +" : Best Individual --> " +
                    " Chromosome : " + Arrays.toString(population.getBestIndividual().getGenes()) +
                    " Fitness Value : " + population.getBestIndividual().getFitness());

            it ++;
        }

    }
}
