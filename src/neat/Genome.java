package neat;
import java.util.*;
/**
 *
 * @author Enrico
 */
public final class Genome {
    protected static int innovationNumber = 0;
    protected static int nodeNumber = 0;
    private static final Random rand = new Random();
    public ArrayList<NodeGene> nodeGenes = new ArrayList<>();
    public ArrayList<ConnectionGene> connectionGenes = new ArrayList<>();
    
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
    }
    
    public ConnectionGene addConnection(int neuronIn, int neuronOut, boolean enabled, double weight, int innovationNumber){
        ConnectionGene newConnection = new ConnectionGene(neuronIn, neuronOut, enabled, weight, innovationNumber);
        connectionGenes.add(newConnection);
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
        NodeGene newNode = new NodeGene(number, type);
        nodeGenes.add(newNode);
        return newNode;
    }
    
    @Override
    public String toString(){
        String s = "Nodes:\n";
        for(NodeGene node: nodeGenes){
            s += String.format("%03d\tType: %s\n", node.number, node.type);
        }
        s += "\nConnections:\n";
        for(ConnectionGene connection: connectionGenes){
            String enabled = (connection.enabled)? "ENBL": "DSBL";
            s += String.format("%03d -> %03d\tWeight: % .2f\t%s\tInn: %03d\n", connection.neuronInNumber, connection.neuronOutNumber, connection.weight, enabled, connection.innovationNumber);
        }
        return s;
    }
}
