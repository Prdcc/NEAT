package neat;

/**
 *
 * @author Enrico
 */
public class ConnectionGene {
    public final int neuronInNumber;
    public final int neuronOutNumber;
    public boolean enabled;
    public double weight;
    
    public ConnectionGene(int neuronIn, int neuronOut, boolean enabled, double weight){
        this.enabled = enabled;
        this.neuronInNumber = neuronIn;
        this.neuronOutNumber = neuronOut;
        this.weight = weight;
    }
    
    public ConnectionGene(ConnectionGene connectionGene){
        this(connectionGene.neuronInNumber, connectionGene.neuronOutNumber, connectionGene.enabled, connectionGene.weight);
    }
}
