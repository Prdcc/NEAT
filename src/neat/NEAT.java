package neat;

/**
 * @author Enrico
 */
public class NEAT {

    public static void main(String[] args) throws NEATException {
        Genome G = new Genome(3,2);
        G.addNode();
        G.addConnection(0,5);
        G.addConnection(5,3);
        NeuralNetwork net = new NeuralNetwork(G);
        System.out.print(net);
    }

}
