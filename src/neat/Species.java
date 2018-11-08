package neat;

import java.util.*;

/**
 *
 * @author Enrico
 */
public class Species {
    public Genome representative;
    private ArrayList<Genome> individuals;
    private static Random rand = new Random();
    public static final double DELTA_THRESHOLD = 3.0; 
    public static final double CULL_FRACTION = 0.5;
    private int currGen = 0;
    private double maxFitnessPast = 0;
    private int genMaxFitness = 0;
    private boolean isOrdered = false;
    
    public Species(ArrayList<Genome> individuals, Genome representative){
        this.representative = representative;
        this.individuals = individuals;
        this.orderByFitness();
    }
    
    public Species(Genome representative){
        this.representative = representative;
        this.individuals = new ArrayList<>();
    }
    
    public Species(Species s){
        this(s.representative);
        for(Genome G: s.individuals){
            individuals.add(new Genome(G));
        }
    }
    
    public void resetSpecies(){
        representative = individuals.get(0);
        individuals.clear();
        currGen++;
        isOrdered = false;
    }
    
    public void addIndividual(Genome G){
        individuals.add(G);
        isOrdered = false;
    }
    
    public boolean removeIndividual(Genome G){
        isOrdered = !individuals.remove(G);
        return !isOrdered;
    }
    
    public Genome removeIndividual(int n){
        isOrdered = false;
        return individuals.remove(n);
    }
    
    public boolean addIfCompatible(Genome G){ //returns true if has been added
        if(representative.getDistance(G) < DELTA_THRESHOLD){
            addIndividual(G);
            return true;
        }
        return false;
    }
    
    public boolean isEmpty(){
        return individuals.isEmpty();
    }
    
    public void orderByFitness(){
        if(!isOrdered){
            individuals.sort(Comparator.comparing(e -> ((Genome) e).fitness).reversed());
            isOrdered = true;
        }
    }
    
    public double getMaxFitness(){
        if(isOrdered){
            return individuals.get(0).fitness;
        }
        return individuals.stream().map(e -> e.fitness).max(Double::compare).get();
    }
    
    public void cullTheWeak(){
        orderByFitness();
        individuals = (ArrayList<Genome>) individuals.subList(0, Math.max((int)(individuals.size() * CULL_FRACTION), 1));
    }
    
    public Genome getRandomIndividual(){
        return individuals.get(rand.nextInt(individuals.size()));
    }
    
    public ArrayList<Genome> getChildren(int n){
        assert !isEmpty(): "Species has no individuals";
        ArrayList<Genome> children = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            children.add(getRandomIndividual().crossover(getRandomIndividual()));
            children.get(i).mutate();
        }
        return children;
    }
    
    public ArrayList<Genome> getChildrenOfTheStrong(int n){
        cullTheWeak();
        return getChildren(n);
    }
    
    public double getTotalFitness(){
        double f = 0;
        for(Genome G: individuals){
            f += G.fitness;
        }        
        return f;
    }
    
    public double getTotalAverageFitness(){
        return getTotalFitness() / individuals.size();
    }
}
