# 🧬 Genetic Algorithm

## Summary
* [Genetic Algorithm Definition](#genetic-algorithm-definition)
* [Genetic Algorithm Pseudocode](#genetic-algorithm-pseudocode)
* [Genetic Algorithm Steps](#genetic-algorithm-steps)
* [Example Implementation in Java to Guess a Word ](#example-implementation-in-java-to-guess-a-word )

### Genetic Algorithm Definition

Genetic algorithm (GA) is a metaheuristic inspired by the process of natural selection that belongs to the larger class of evolutionary 
algorithms (EA). Genetic algorithms are commonly used to generate high-quality solutions to optimization and search problems by relying 
on biologically inspired operators such as mutation, crossover and selection .

### Genetic Algorithm Pseudocode

```java
START
Generate the initial population
Compute fitness
REPEAT
    Selection
    Crossover
    Mutation
    Compute fitness
UNTIL population has converged
STOP
```

<table>
<tr>
<td width="100%">
          <p align="center">
             <img src="https://user-images.githubusercontent.com/85403056/237043125-dfbafd11-d1f0-40ff-b0e8-7e65666bdd09.png" alt="project example"/>
            </p>
 </td>
</tr>
</table>

### Essential Terms

```
Population 
Chromosome
Genes
Encoding Methods
Fitness Function
```
<table>
<tr>
<td width="50%">
          <p align="center">
             <img src="https://user-images.githubusercontent.com/85403056/237047695-b467ee2a-0181-465d-a1bb-70541769cab1.png" alt="project example"/>
            </p>
 </td>
</tr>
</table>

### 3. Genetic Algorithm Steps

Five phases are considered in a genetic algorithm :
1. Initial population
2. Fitness function
3. Selection
4. Crossover
5. Mutation

> Initial Population

The process begins with a set of individuals which is called a Population. Each individual is a solution to the problem you want to solve.
An individual is characterized by a set of parameters (variables) known as Genes. Genes are joined into a string to form a Chromosome (solution).

> Fitness Function

The fitness function determines how fit an individual is (the ability of an individual to compete with other individuals). It gives a fitness score to each individual. The probability that an individual will be selected for reproduction is based on its fitness score.

> Selection

The idea of selection phase is to select the fittest individuals and let them pass their genes to the next generation.
Two pairs of individuals (parents) are selected based on their fitness scores.

> Crossover

Crossover is the most significant phase in a genetic algorithm. For each pair of parents to be mated, a crossover point is chosen at random from within the genes.

<table>
<tr>
<td width="40%">
          <p align="center">
             <img src="https://user-images.githubusercontent.com/85403056/237050325-b5941b85-1382-465e-bd66-77603a83baa1.png" alt="project example" width = "400"/>
            </p>
 </td>
</tr>
</table>

<table>
<tr>
<td width="50%">
          <p align="center">
             <h5> Offspring are created by exchanging the genes of parents among themselves until the crossover point is reached. </h5>
                    &nbsp;
             <img src="https://user-images.githubusercontent.com/85403056/237050657-f050ab7b-532e-408b-8c15-d367066c5c19.png" alt="project example"/>
            </p>
 </td>
 
 <td width="50%">
          <p align="center">
             <h5> The new Offspring are added to the population. </h5>
                    &nbsp;
             <img src="https://user-images.githubusercontent.com/85403056/237052181-61fe4a72-d0e3-441e-b6f0-9e055b5275b0.png" alt="project example"/>
            </p>
 </td>
</tr>
</table>
          
> Mutation

Mutation occurs to maintain diversity within the population and prevent premature convergence.
<table>
<tr>
<td width="40%">
          <p align="center">
             <img src="https://user-images.githubusercontent.com/85403056/237054212-cf57d172-7756-41f3-98bb-640bbe72d6eb.png" alt="project example" width = "400"/>
            </p>
 </td>
    
</tr>
</table>

> Termination

The algorithm terminates if the population has converged. Then it is said that the genetic algorithm has provided a set of solutions to our problem.

## Example Implementation in Java to Guess a Word 

1. Initial population 

   - Create Individual :
   
   ```java
   package ma.enset.entites;

   import java.util.Random;

   public class Individual implements Comparable{

    // chromosome
    private char genes[] = new char[4];
    private int fitness;

    private String target = "sdia";

    private String alphabets = "abcdefghijklmnopqrstuvwxyz";

    public Individual() {
        for (int i=0 ; i < genes.length ; i++){
            genes[i] = alphabets.charAt(new Random().nextInt(alphabets.length()));
        }
    }
    // Calculate Fitness Value Function
    public int getFitness() {
        return fitness;
    }

    public char[] getGenes() {
        return genes;
    }


    @Override
    public int compareTo(Object o) {
        Individual individual = (Individual) o;
        if (this.getFitness() < ((Individual) o).getFitness()) return -1;
        else if (this.getFitness() > ((Individual) o).getFitness()) return 1;
        return 0;
    }
   }

   ```
   - Initialize Population :
   
   ```java
   public void initializePopulation(){
        for (int i = 0; i < populaion ; i++) {
            individuals.add(new Individual());
        }
    }
   ```
2. Fitness function

   ```java
   public void calculateFitness(){
        fitness = 0;
        int fitnessValues[] = new int[4];
        int i = 0;
        for (int gene : genes) {
            int geneValueFromTarget = gene - target.charAt(i);
            if (geneValueFromTarget < 0) geneValueFromTarget = Math.abs(geneValueFromTarget);
            fitnessValues[i] = geneValueFromTarget;
            i ++;
        }
        for (int fv: fitnessValues) {
            fitness += fv;
        }
    }
   ```
3. Selection

   ```java
   public void selection(){
        firstFitness = individuals.get(0);
        secondFitness = individuals.get(1);
    }
   ```
4. Crossover

   ```java
   public void crossover(){
        int crossoverPoint = 1 + new Random().nextInt(4);

        Individual individual1 = new Individual();
        Individual individual2 = new Individual();

        for (int i = 0; i < individual1.getGenes().length; i++) {
            individual1.getGenes()[i] = firstFitness.getGenes()[i];
            individual2.getGenes()[i] = secondFitness.getGenes()[i];
        }

        for (int i = 0; i < crossoverPoint; i++) {
            individual1.getGenes()[i] = secondFitness.getGenes()[i];
            individual2.getGenes()[i] = firstFitness.getGenes()[i];
        }

        individuals.set(0, individual1);
        individuals.set(1, individual2);

        //System.out.println("Crossover Point : " + crossoverPoint);
    }
   ```
5. Mutation

   ```java
   public void mutation(){
        int index = random.nextInt(4);
        for (int i = 0; i <target.length(); i++) {
            if(individuals.get(0).getGenes()[index] != target.charAt(i))
                individuals.get(0).getGenes()[index] = target.charAt(random.nextInt(4));

            index = random.nextInt(4);
            if(individuals.get(1).getGenes()[index] != target.charAt(i))
                individuals.get(1).getGenes()[index] = target.charAt(random.nextInt(4));
        }
    }
   ```
   
   * TEST : 
   Word to Guess `sdia`
   
<table>
<tr>
<td width="50%">
          <p align="center">
             <img src="https://user-images.githubusercontent.com/85403056/237059504-8489439b-d49f-4046-ba92-9293a6c5f887.png" alt="project example"/>
            </p>
 </td>
</tr>
</table>

