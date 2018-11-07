package neat;

/**
 *
 * @author Enrico
 */
public class NodeGene {
    public enum NodeType{
        INPUT("IN"),
        OUTPUT("OUT"),
        HIDDEN("HDN");
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
    public int layer = -1;
    
    
    public NodeGene(NodeType type){
        this.type = type;
    }
    
    public NodeGene(NodeType type, int layer){
        this(type);
        this.layer = layer;
    }
    
    public NodeGene(NodeGene nodeGene){
        this(nodeGene.type, nodeGene.layer);
    }
    
    public int getLayer(){
        return layer;
    }
}
