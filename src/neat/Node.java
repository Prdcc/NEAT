package neat;

import java.util.*;

/**
 *
 * @author Enrico
 */
public class Node extends NodeGene{
    public double value;
    public ArrayList<ConnectionGene> incomingConnections = new ArrayList<>();
    
    public Node(NodeType type) {
        this(type, 0);
    }
    
    public Node(NodeType type, double value) {
        super(type);
        this.value = value;
    }
    
    public Node(NodeGene nodeInfo){
        this(nodeInfo, 0);
    }
    
    public Node(NodeGene nodeInfo, double value){
        super(nodeInfo);
        this.value = value;
    }
    
    
    @Override
    public String toString(){
        String connections = "\nConnections:\n";
        for(ConnectionGene conn: incomingConnections){
            connections += conn.toString() + "\n";
        }
        return String.format("\tType: %s%s\n", type, connections);
    }
}
