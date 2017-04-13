package core;

import java.util.ArrayList;
import java.util.List;

/*** Represents a NS diagram. Contains multiple nested blocks that represent the
 * branching logic of the program.
 * 
 * @author Bjorn
 *
 */

public class NassiShneiderman {
	
	// the set of blocks that forms the digram
	
	List<NassiShneidermanBlock> drawingBlocks;
	
	public NassiShneiderman() {
		drawingBlocks = new ArrayList<NassiShneidermanBlock>();
	}
	
	public void addBlock(NassiShneidermanBlock block) {
		drawingBlocks.add(block);
	}
	
	public String renderDiagram() {
		String diagramXML = "";
		
		for (NassiShneidermanBlock renderingBlock : drawingBlocks) {
			diagramXML = diagramXML + "\n" + renderingBlock.renderBlock();
		}
		
		return diagramXML;
	}
	
	
	
}
