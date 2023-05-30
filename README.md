# 🧬 Genetic Algorithm Implementation with Java / Jade
```
The repository contains three versions of a Genetic Algorithm implementation in Java. The first version
is a sequential implementation, the second version utilizes SMA, and the third version incorporates an 
Island Model, allowing for parallel execution and intercommunication between multiple populations.
These implementations provide different approaches for solving optimization problems using genetic 
algorithms in a Java environment.
```
## Table of Contents
- [Genetic Algorithm](#genetic-algorithm)
    - [Definition](#definition)
    - [Pseudocode](#pseudocode)
    - [Steps](#steps)
- [Sequential Genetic Algorithm](#sequential-genetic-algorithm)
- [Genetic Algorithm with SMA](#genetic-algorithm-with-sma)
- [Genetic Algorithm with Island Model](#genetic-algorithm-with-island-model)

# Genetic Algorithm
### Definition

Genetic algorithm (GA) is a metaheuristic inspired by the process of natural selection that belongs to the larger class of evolutionary 
algorithms (EA). Genetic algorithms are commonly used to generate high-quality solutions to optimization and search problems by relying 
on biologically inspired operators such as mutation, crossover and selection .

### Pseudocode

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

### Steps

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

# Sequential Genetic Algorithm
[Link to project](https://github.com/el-moudni-hicham/genetic-algorithm-java/tree/master/src/ma/enset/sequential)

### Project Structure 

<pre>
D:.
├───application
│       GA.java
│
├───entites
│       Individual.java
│       Population.java
│
└───helpers
        GAUtils.java
</pre>

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
   Word to Guess `Hicham EL MOUDNI`

<table>
<tr>
<td width="50%">
          <p align="center">
             <img src="https://github.com/el-moudni-hicham/genetic-algorithm-java/assets/85403056/5f831fd0-2a1d-41f4-ba9a-8e6211710732" alt="project example"/>
            </p>
 </td>
</tr>
</table>

# Genetic Algorithm with SMA

[Link to project](https://github.com/el-moudni-hicham/genetic-algorithm-java/tree/master/src/ma/enset/sma)

In this approch we use an agent for each chromosome.

### Project Structure 

<pre>
D:.
├───agents
│       CentralAgent.java
│       IndividualAgent.java
│
├───containers
│       CentralContainer.java
│       MainContainer.java
│       SimpleContainer.java
│
└───helpers
        FitnessAgent.java
        GAUtils.java
</pre>

- TEST : 
   Word to Guess `Hicham EL MOUDNI`
   
   
<table>
<tr>
<td width="50%">
          <p align="center">
             <img src="https://github.com/el-moudni-hicham/genetic-algorithm-java/assets/85403056/e20c0d4e-fe8f-444d-9901-96ad5beef83c" alt="project example"/>
            </p>
 </td>
</tr>
</table>
   



# Genetic Algorithm with Island Model
[Link to project](https://github.com/el-moudni-hicham/genetic-algorithm-java/tree/master/src/ma/enset/island_model)

```
Island Model Genetic Algorithm (IMGA) is a distributed model of GA which splits its main computational
process into several computers (islands) instead of running it in only one machine. This mechanism 
offers higher scalability and gives better chance to evade the local optimum trap.
```

Parallel genetic algorithm

Parallel genetic algorithm is such an algorithm that uses multiple genetic algorithms to solve a single task .
All these algorithms try to solve the same task and after they’ve completed their job, the best individual of 
every algorithm is selected, then the best of them is selected, and this is the solution to a problem. This is
one of the most popular approach to parallel genetic algorithms, even though there are others. This approach is
often called ‘island model’ because populations are isolated from each other, like real-life creature populations 
may be isolated living on different islands. Image 1 illustrates that.

<table>
<tr>
<td width="50%">
          <p align="center">
             <img src="https://github.com/el-moudni-hicham/genetic-algorithm-java-jade/assets/85403056/a827e49d-c3f4-4b8c-a709-352de4f1b9b3" alt="project example"/>
            </p>
 </td>
</tr>
</table>

### Project Structure 

<pre>
D:.
├───agents
│       IslandAgent.java
│       MasterAgent.java
│
├───containers
│       MainContainer.java
│       MasterContainer.java
│       SimpleContainer.java
│
├───entites
│       Individual.java
│
└───helpers
        GAUtils.java
</pre>

- TEST : 
   Word to Guess `Hicham EL MOUDNI`
   
![island-model](https://github.com/el-moudni-hicham/genetic-algorithm-java/assets/85403056/38c9fa33-4f70-44a1-9f24-a8a98f14e5ef)

A parallel genetic algorithm may take a little more time than a non-parallel one, that is because is uses several
computation threads which, in turn, cause the Operation System to perform context switching more frequently. 
Nevertheless, parallel genetic algorithm tend to produce better results and more optimal individuals than 
a non-parallel one.


