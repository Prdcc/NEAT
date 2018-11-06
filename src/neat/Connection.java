package neat;

/**
 *
 * @author Enrico
 */
public class Connection extends ConnectionGene{
    public Node neuronIn;
    public Node neuronOut;
    public Connection(Node neuronIn, Node neuronOut, boolean enabled, double weight, int innovationNumber) {
        super(neuronIn.number, neuronOut.number, enabled, weight, innovationNumber);
        this.neuronIn = neuronIn;
        this.neuronOut = neuronOut;
    }
    
    public Connection(ConnectionGene connectionGene, Node neuronIn, Node neuronOut){
        super(connectionGene);
        this.neuronIn = neuronIn;
        this.neuronOut = neuronOut;
    }
    
    @Override
    public String toString(){
        return String.format("%s -> %s   Weight: %f", neuronInNumber, neuronOutNumber, weight);
    }
}
