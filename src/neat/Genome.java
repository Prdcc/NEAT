package neat;
import java.util.*;
import java.util.Map.Entry;
/**
 *
 * @author Enrico
 */
public final class Genome {
    protected static int innovationNumber = 0;
    protected static int nodeNumber = 0;
    private static final Random rand = new Random();
    public Map<Integer, NodeGene> nodeGenes = new HashMap<>();
    public Map<Integer, ConnectionGene> connectionGenes = new HashMap<>();
    
    public Genome(int numberOfInputs, int numberOfOutputs){ //create starting genome
        for(int i = 0; i < numberOfInputs; i++){
            addNode(NodeGene.NodeType.INPUT);
        }
        for(int i = 0; i < numberOfOutputs; i++){
            addNode(NodeGene.NodeType.OUTPUT);
        }
        
        for(int in = 0; in < numberOfInputs; in++){
            for(int out = numberOfInputs; out < 
                    numberOfOutputs + numberOfInputs; out++){
                addConnection(in, out, rand.nextGaussian() / Math.sqrt(numberOfInputs));
            }
        }
        
        updateLayers();
    }
    
    public ConnectionGene addConnection(int neuronIn, int neuronOut, boolean enabled, double weight, int innovationNumber){
        ConnectionGene newConnection = new ConnectionGene(neuronIn, neuronOut, enabled, weight);
        connectionGenes.put(innovationNumber, newConnection);
        updateLayers();
        return newConnection;
    }
    
    public ConnectionGene addConnection(int neuronIn, int neuronOut){
        return addConnection(neuronIn, neuronOut, true, rand.nextGaussian(), innovationNumber++);
    }
    
    public ConnectionGene addConnection(int neuronIn, int neuronOut, double weight){
        return addConnection(neuronIn, neuronOut, true, weight, innovationNumber++);
    }
    
    public NodeGene addNode(){
        return addNode(nodeNumber++, NodeGene.NodeType.HIDDEN);
    }
    
    public NodeGene addNode(NodeGene.NodeType type){
        return addNode(nodeNumber++, type);
    }
    
    public NodeGene addNode(int number, NodeGene.NodeType type){
        NodeGene newNode = new NodeGene(type);
        nodeGenes.put(number, newNode);
        return newNode;
    }
    
    @Override
    public String toString(){
        String s = "Nodes:\n";
        for(Entry<Integer, NodeGene> entry: nodeGenes.entrySet()){
            s += String.format("%03d\tType: %s\tLayer: %02d\n", entry.getKey(), entry.getValue().type, entry.getValue().layer);
        }
        s += "\nConnections:\n";
        for(Entry<Integer, ConnectionGene> entry: connectionGenes.entrySet()){
            String enabled = (entry.getValue().enabled)? "ENBL": "DSBL";
            s += String.format("%03d -> %03d\tWeight: % .2f\t%s\tInn: %03d\n", entry.getValue().neuronInNumber, entry.getValue().neuronOutNumber, entry.getValue().weight, enabled, entry.getKey());
        }
        return s;
    }

    private int calculateLayer(int geneId) {
        NodeGene gene = nodeGenes.get(geneId);
        if(gene.layer >= 0){return gene.layer;}
        if(gene.type == NodeGene.NodeType.INPUT){
            return 0;
        }
        else{
            int max = 0;
            for(ConnectionGene conn: connectionGenes.values()){
                if(conn.neuronOutNumber == geneId && conn.neuronInNumber != geneId){
                    int tempLayer = calculateLayer(conn.neuronInNumber);
                    if(tempLayer > max){
                        max = tempLayer;
                    }
                }
            }
            return max+1;
        }
    }

    private void updateLayers() {
        for(NodeGene node: nodeGenes.values()){
            node.layer = -1;
        }
        for(Entry<Integer, NodeGene> entry: nodeGenes.entrySet()){
            entry.getValue().layer = calculateLayer(entry.getKey());
        }
    }
}
