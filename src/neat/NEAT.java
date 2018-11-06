package neat;

/**
 * @author Enrico
 */
public class NEAT {

    public static void main(String[] args) throws NEATException {
        Genome G = new Genome(5, 3);
        G.connectionGenes.get(0).enabled= false;
        NeuralNetwork net = new NeuralNetwork(G);
        System.out.print(net);
    }

}
