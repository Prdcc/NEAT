package neat;

/**
 *
 * @author Enrico
 */
public class NodeGene {
    public final int number;
    public enum NodeType{
        INPUT("input"),
        OUTPUT("output"),
        HIDDEN("hidden");
        private final String name;
        private NodeType(String name){
            this.name = name;
        }
        @Override
        public String toString(){
            return name;
        }
    }
    public final NodeType type;
    
    
    public NodeGene(int n, NodeType type){
        this.number = n;
        this.type = type;
    }
    
    public NodeGene(NodeGene nodeGene){
        this(nodeGene.number, nodeGene.type);
    }
}
