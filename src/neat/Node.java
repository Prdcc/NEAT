/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neat;

import java.util.*;

/**
 *
 * @author Enrico
 */
public class Node extends NodeGene{
    public double value;
    public ArrayList<Connection> incomingConnections = new ArrayList<>();
    
    public Node(int n, NodeType type) {
        this(n, type, 0);
    }
    
    public Node(int n, NodeType type, double value) {
        super(n, type);
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
        for(Connection conn: incomingConnections){
            connections += conn.toString() + "\n";
        }
        return String.format("%03d\tType: %s%s\n", number, type, connections);
    }
}
