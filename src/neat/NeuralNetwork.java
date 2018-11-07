package neat;

import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Enrico
 */
public class NeuralNetwork {

    public final Genome genome;
    public Map<Integer, Node> nodes = new HashMap<>();
    public ArrayList<Node> nodesByLayer = new ArrayList<>();
    private final ArrayList<Node> inputs = new ArrayList<>();
    private final ArrayList<Node> outputs = new ArrayList<>();

    public NeuralNetwork(Genome genome) throws NEATException {
        this.genome = genome;
        for (Entry<Integer, NodeGene> entry : genome.nodeGenes.entrySet()) {
            nodes.put(entry.getKey(), new Node(entry.getValue()));
        }
        for (ConnectionGene gene : genome.connectionGenes.values()) {
            if (gene.enabled) {
                nodes.get(gene.neuronOutNumber).incomingConnections.add(gene);
            }
        }

        nodes.values().stream().sorted(Comparator.comparing(Node::getLayer)).
                forEach(nodesByLayer::add);
        
        for (int k = 0; getNode(k).type == NodeGene.NodeType.INPUT; k++) {
            inputs.add(getNode(k));
        }
        
        for (int k = inputs.size(); getNode(k).type == NodeGene.NodeType.OUTPUT; k++) {
            outputs.add(getNode(k));
        }
    }

    public ArrayList<Double> evaluateNetwork(ArrayList<Double> inputValues) throws NEATException {
        for (int i = 0; i < inputValues.size(); i++) {
            nodes.get(i).value = inputValues.get(i);
        }

        for (Node node : nodesByLayer) {
            double sum = 0;
            for (ConnectionGene conn : node.incomingConnections) {
                sum += conn.weight * getNode(conn.neuronInNumber).value;
            }
            node.value = sum;
        }
        ArrayList<Double> temp = new ArrayList<>(outputs.size());
        for (int k = 0; k < outputs.size(); k++) {
            temp.add(outputs.get(k).value);
        }

        return temp;
    }

    public Node getNode(int n) throws NEATException {
        if (nodes.containsKey(n)) {
            return nodes.get(n);
        }
        throw new NEATException(NEATException.ExceptionCode.NODENOTFOUND);
    }

    @Override
    public String toString() {
        String s = "Nodes:\n";
        for (Entry<Integer, Node> entry : nodes.entrySet()) {
            s += String.format("%03d Type: %s\tValue: %.2f\n", entry.getKey(), entry.getValue().type, entry.getValue().value);
        }
        s += "\nConnections:\n";
        for (ConnectionGene connection : genome.connectionGenes.values()) {
            s += String.format("%03d -> %03d   Weight: % .2f\n", connection.neuronInNumber, connection.neuronOutNumber, connection.weight);
        }
        return s;
    }
}
