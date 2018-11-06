package neat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import neat.NodeGene.NodeType;


public class GenomePrinter {
	
	public static void printGenome(Genome genome, String path) {
		Random r = new Random();
		HashMap<Integer, Point> nodeGenePositions = new HashMap<>();
		int nodeSize = 20;
		int connectionSizeBulb = 6;
		int imageSize = 512;
		
		BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imageSize, imageSize);
		
		g.setColor(Color.BLUE);
		for (NodeGene gene : genome.nodeGenes) {
			if (null != gene.type) switch (gene.type) {
                        case INPUT:{
                            float x = ((float)(gene.number+1)/((float)countNodesByType(genome, NodeType.INPUT)+1f)) * imageSize;
                            float y = imageSize-nodeSize/2;
                            g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
                            nodeGenePositions.put(gene.number, new Point((int)x,(int)y));
                                break;
                            }
                        case HIDDEN:{
                            int x = r.nextInt(imageSize-nodeSize*2)+nodeSize;
                            int y = r.nextInt(imageSize-nodeSize*3)+(int)(nodeSize*1.5f);
                            g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
                            nodeGenePositions.put(gene.number, new Point((int)x,(int)y));
                                break;
                            }
                        case OUTPUT:{
                            int x = r.nextInt(imageSize-nodeSize*2)+nodeSize;
                            int y = nodeSize/2;
                            g.fillOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
                            nodeGenePositions.put(gene.number, new Point((int)x,(int)y));
                                break;
                            }
                        default:
                            break;
                    }
		}
		
		
		for (ConnectionGene gene : genome.connectionGenes) {
			if (!gene.enabled) {
				continue;
			}
			Point inNode = nodeGenePositions.get(gene.neuronInNumber);
			Point outNode = nodeGenePositions.get(gene.neuronOutNumber);
			
			Point lineVector = new Point((int)((outNode.x - inNode.x) * 0.95f), (int)((outNode.y - inNode.y) * 0.95f));
			g.setStroke(new BasicStroke((int) (3 * Math.abs(gene.weight))));
                        g.setColor((gene.weight < 0)? Color.red: Color.green);
			g.drawLine(inNode.x, inNode.y, inNode.x+lineVector.x, inNode.y+lineVector.y);
			g.fillRect(inNode.x+lineVector.x-connectionSizeBulb/2, inNode.y+lineVector.y-connectionSizeBulb/2, connectionSizeBulb, connectionSizeBulb);
			//g.drawString(""+gene.weight, (int)(inNode.x+lineVector.x*0.25f+5), (int)(inNode.y+lineVector.y*0.25f));
		}
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.WHITE);
		for (NodeGene nodeGene : genome.nodeGenes) {
			Point p = nodeGenePositions.get(nodeGene.number);
			g.drawString(""+nodeGene.number, p.x-5, p.y+5);
		}
		
		
		try {
			ImageIO.write(image, "PNG", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int countNodesByType(Genome genome, NodeType type) {
		int c = 0;
		for (NodeGene node : genome.nodeGenes) {
			if (node.type == type) {
				c++;
			}
		}
		return c;
	}

}
