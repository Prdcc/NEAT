package neat;

import java.util.*;
import java.util.Map.Entry;
import javafx.util.Pair;

/**
 *
 * @author Enrico
 */
public final class Genome {
    public static final double RESET_WEIGHT_CHANCE = 0.1;
    public static final double WEIGHT_JIGGLE_SCALE = 0.1;
    public static final double C1 = 1.0;
    public static final double C2 = 1.0;
    public static final double C3 = 0.1;
    protected static int innovationNumber = 0;
    protected static int nodeNumber = 0;
    private static final Random rand = new Random();
    public Map<Integer, NodeGene> nodeGenes = new HashMap<>();
    public Map<Integer, ConnectionGene> connectionGenes = new HashMap<>();
    public double fitness;
    

    public Genome(int numberOfInputs, int numberOfOutputs) { //create starting genome
        for (int i = 0; i < numberOfInputs; i++) {
            addNode(NodeGene.NodeType.INPUT);
        }
        for (int i = 0; i < numberOfOutputs; i++) {
            addNode(NodeGene.NodeType.OUTPUT);
        }

        for (int in = 0; in < numberOfInputs; in++) {
            for (int out = numberOfInputs; out
                    < numberOfOutputs + numberOfInputs; out++) {
                addConnection(in, out, rand.nextGaussian() / Math.sqrt(numberOfInputs));
            }
        }

        updateLayers();
        fitness = 0;
    }

    public Genome(Genome toBeCopied) {
        for (Integer index : toBeCopied.nodeGenes.keySet()) {
            nodeGenes.put(index, new NodeGene(toBeCopied.nodeGenes.get(index)));
        }

        for (Integer index : toBeCopied.connectionGenes.keySet()) {
            connectionGenes.put(index, new ConnectionGene(toBeCopied.connectionGenes.get(index)));
        }
        fitness = 0;
    }

    public ConnectionGene addConnection(int neuronIn, int neuronOut, boolean enabled, double weight, int innovationNumber) {
        ConnectionGene newConnection = new ConnectionGene(neuronIn, neuronOut, enabled, weight);
        connectionGenes.put(innovationNumber, newConnection);
        updateLayers();
        return newConnection;
    }

    public ConnectionGene addConnection(int neuronIn, int neuronOut) {
        return addConnection(neuronIn, neuronOut, true, rand.nextGaussian(), innovationNumber++);
    }

    public ConnectionGene addConnection(int neuronIn, int neuronOut, double weight) {
        return addConnection(neuronIn, neuronOut, true, weight, innovationNumber++);
    }

    public Entry<Integer, NodeGene> addNode() {
        return addNode(nodeNumber++, NodeGene.NodeType.HIDDEN);
    }

    public Entry<Integer, NodeGene> addNode(NodeGene.NodeType type) {
        return addNode(nodeNumber++, type);
    }

    public Entry<Integer, NodeGene> addNode(int number, NodeGene.NodeType type) {
        NodeGene newNode = new NodeGene(type);
        nodeGenes.put(number, newNode);
        return new AbstractMap.SimpleEntry<>(number, newNode);
    }

    @Override
    public String toString() {
        String s = "Nodes:\n";
        for (Entry<Integer, NodeGene> entry : nodeGenes.entrySet()) {
            s += String.format("%03d\tType: %s\tLayer: %02d\n", entry.getKey(), entry.getValue().type, entry.getValue().layer);
        }
        s += "\nConnections:\n";
        for (Entry<Integer, ConnectionGene> entry : connectionGenes.entrySet()) {
            String enabled = (entry.getValue().enabled) ? "ENBL" : "DSBL";
            s += String.format("%03d -> %03d\tWeight: % .2f\t%s\tInn: %03d\n", entry.getValue().neuronInNumber, entry.getValue().neuronOutNumber, entry.getValue().weight, enabled, entry.getKey());
        }
        return s;
    }

    private int calculateLayer(int geneId) {
        NodeGene gene = nodeGenes.get(geneId);
        if (gene.layer >= 0) {
            return gene.layer;
        }
        if (gene.type == NodeGene.NodeType.INPUT) {
            return 0;
        } else {
            int max = 0;
            for (ConnectionGene conn : connectionGenes.values()) {
                if (conn.neuronOutNumber == geneId && conn.neuronInNumber != geneId) {
                    int tempLayer = calculateLayer(conn.neuronInNumber);
                    if (tempLayer > max) {
                        max = tempLayer;
                    }
                }
            }
            return max + 1;
        }
    }

    private void updateLayers() {
        for (NodeGene node : nodeGenes.values()) {
            node.layer = -1;
        }
        for (Entry<Integer, NodeGene> entry : nodeGenes.entrySet()) {
            entry.getValue().layer = calculateLayer(entry.getKey());
        }
    }

    public static Genome crossover(Genome G, Genome H){
        Genome greaterFitness = (G.fitness > H.fitness)? G: H;
        Genome lowerFitness = (G.fitness > H.fitness)? H: G;
        Genome returnVal = new Genome(greaterFitness);
        for(Entry<Integer, ConnectionGene> entry: returnVal.connectionGenes.entrySet()){
            if(rand.nextBoolean() && lowerFitness.connectionGenes.containsKey(entry.getKey())){
                entry.setValue(lowerFitness.connectionGenes.get(entry.getKey()));
            }
        }
        
        //ADD ENABLE DISABLE GENE
        return returnVal;
    }
    
    public Genome crossover(Genome H){
        return crossover(this, H);
    }
    
    //add node from mutation
    public void addNode(ConnectionGene conn){
        int nodeID = addNode(Node.NodeType.HIDDEN).getKey();
        conn.disable();
        addConnection(conn.neuronInNumber, nodeID, conn.weight);
        addConnection(nodeID, conn.neuronOutNumber, 1.0);
    }
    
    public void changeWeight(ConnectionGene conn){
        if(rand.nextDouble() < RESET_WEIGHT_CHANCE){
            conn.weight = rand.nextGaussian();
        }
        else{
            conn.weight += rand.nextGaussian() * WEIGHT_JIGGLE_SCALE;
        }
    }
    
    public int numberOfGenes(){
        return connectionGenes.size() + nodeGenes.size();
    }
    
    private static Pair<Integer, Integer> getExcessDisjoint(Set<Integer> s1, Set<Integer> s2){
        int E = 0, D = 0;
        int maxBeforeExcess = Math.min(Collections.max(s1), Collections.max(s2));
        for(Integer key: s1){
            if(key > maxBeforeExcess){
                E++;
                continue;
            }
            if(!s2.contains(key)){
                D++;
            }
        }
        
        for(Integer key: s2){
            if(key > maxBeforeExcess){
                E++;
                continue;
            }
            if(!s1.contains(key)){
                D++;
            }
        }
        return new Pair<>(E, D);
    }
    
    public static double getDistance(Genome G, Genome H){
        double distance = 0;
        double N = 1.0 / Math.max(G.numberOfGenes(), H.numberOfGenes());
        int E = 0;
        int D = 0;
        
        Pair<Integer, Integer> nodeDiff = getExcessDisjoint(G.nodeGenes.keySet(), H.nodeGenes.keySet());
        E += nodeDiff.getKey();
        D += nodeDiff.getValue();
        
        Pair<Integer, Integer> connDiff = getExcessDisjoint(G.connectionGenes.keySet(), H.connectionGenes.keySet());
        E += connDiff.getKey();
        D += connDiff.getValue();
        
        double W = 0;
        int i = 0;
        for(Entry<Integer, ConnectionGene> entry: G.connectionGenes.entrySet()){
            if(H.connectionGenes.containsKey(entry.getKey())){
                W += Math.abs(entry.getValue().weight - H.connectionGenes.get(entry.getKey()).weight);
                i++;
            }
        }
        W /= i;
        distance = C1*E*N + C2*D*N + C3*W;
        return distance;
    }   
    
    public double getDistance(Genome G){
        return getDistance(this, G);
    }
}
