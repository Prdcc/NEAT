package neat;
import java.util.*;
/**
 *
 * @author Enrico
 */
public class NeuralNetwork {
    public final Genome genome;
    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Connection> connections = new ArrayList<>();
    public Map<Integer, Node> nodeDictionary = new HashMap<>();
    
    public NeuralNetwork(Genome genome) throws NEATException{
        this.genome = genome;
        for (NodeGene nodeGene : genome.nodeGenes) {
            Node temp = new Node(nodeGene);
            nodes.add(temp);
            nodeDictionary.put(temp.number, temp);
        }
        for(ConnectionGene gene: genome.connectionGenes){
            if(gene.enabled){
                Connection temp = new Connection(gene, getNode(gene.neuronInNumber), getNode(gene.neuronOutNumber));
                connections.add(temp);
                temp.neuronOut.incomingConnections.add(temp);
            }
        }
    }
    
    public Node getNode(int n) throws NEATException{
        if(nodeDictionary.containsKey(n)){
            return nodeDictionary.get(n);
        }
        throw new NEATException(NEATException.ExceptionCode.NODENOTFOUND);
    }
    
    @Override
    public String toString(){
        String s = "Nodes:\n";
        for(Node node: nodes){
            s += String.format("%03d Type: %s\n", node.number, node.type);
        }
        s += "\nConnections:\n";
        for(Connection connection: connections){
            s += String.format("%03d -> %03d   Weight: %f\n", connection.neuronInNumber, connection.neuronOutNumber, connection.weight);
        }
        return s;
    }
}
