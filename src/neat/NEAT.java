package neat;

/**
 * @author Enrico
 */
public class NEAT {

    public static void main(String[] args) throws NEATException {
        Genome G = new Genome(5, 3);
        G.addNode(NodeGene.NodeType.HIDDEN);
        G.addConnection(8, 10);
        G.addConnection(1,8);
        G.addNode(NodeGene.NodeType.HIDDEN);
        G.addConnection(9, 10);
        G.addConnection(1,9);
        G.addNode(NodeGene.NodeType.HIDDEN);
        G.addConnection(10, 5);
        G.addConnection(1,10);
        NeuralNetwork net = new NeuralNetwork(G);
        //System.out.print(net);
        GenomePrinter.printGenome(G, "test.png");
    }

}
