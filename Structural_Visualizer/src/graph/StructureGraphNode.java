package graph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import core.NassiShneidermanBlock;
import core.NassiShneidermanExprBlock;
import core.NassiShneidermanForBlock;
import core.NassiShneidermanIfBlock;
import core.NassiShneidermanTryBlock;
import core.NassiShneidermanWhileBlock;

/*** A simple node class to connect drawn blocks and provide child ordering. The node
 * can also store auxilliary information to help with tasks such as block highlighting
 * and sizing the blocks.
 * 
 * @author Bjorn
 *
 */

public class StructureGraphNode {
	private List<StructureGraphNode> orderedChildren;
	private Integer nodeStartLine;
	private Integer nodeEndLine;
	
	private Integer drawnBlockHeight;
	
	private NassiShneidermanBlock blockToDraw;
	
	private String compartmentDescription;
	
	private Integer heightSpy;
	private Integer widthSpy;
	
	private StructureGraphNode parentNode;
	
	public StructureGraphNode() {
		orderedChildren = new ArrayList<StructureGraphNode>();
		
		nodeStartLine = 0;
		nodeEndLine = 0;
		
		compartmentDescription = "";
		
		heightSpy = 0;
		widthSpy = 0;
	}
	
	public void addChild(StructureGraphNode newChild) {
		orderedChildren.add(newChild);
		newChild.parentNode = this;
	}
	
	public List<StructureGraphNode> getOrderedChildren() {
		return orderedChildren;
	}
	
	public String getCompartmentDescription() {
		return compartmentDescription;
	}
	
	/*** Count the transient children of the given node
	 * 
	 * @return number of children at the node level and below, recursively
	 */
	
	public Integer recursiveCountChildren() {
		
		Integer myCount = 0;
		
		for (StructureGraphNode child : this.getOrderedChildren()) {
			myCount = myCount + child.recursiveCountChildren();
		}
		
		return myCount;
	}
	
	/** Recursively calculate and store height and width for graph nodes
	 * 
	 * @param drawWidth Given width from parent
	 * @return Height calculated for child
	 */
	
	// TODO: If we are already recursing the graph, shouldn't we be able to calculate
	// more properties than height along the way?
	
	public Integer setOuterBoxDimensions(Integer drawWidth) {
		// Basic block height
		Integer myHeight = 0;
		
		/* A recursive algorithm with a twist: need to have the various 
		 * compartments of certain nodes 
		 * 
		 */
		
		if (blockToDraw instanceof NassiShneidermanIfBlock) {
			// use height of tallest side to set height - add room for headers
			
			myHeight = myHeight + 80;
			
			Integer trueHeight = 0;
			Integer falseHeight = 0;
			
			for (StructureGraphNode child : this.getOrderedChildren()) {
				if (child.getCompartmentDescription().equals("If true side")) {
					trueHeight = trueHeight + child.setOuterBoxDimensions(drawWidth / 2);
				}
				else if (child.getCompartmentDescription().equals("If false side")) {
					falseHeight = falseHeight + child.setOuterBoxDimensions(drawWidth / 2);
				}
			}
			
			myHeight = myHeight + Math.max(trueHeight, falseHeight);
		}
		
		else if (blockToDraw instanceof NassiShneidermanTryBlock) {
			
			Integer catchCount = 0;
			
			for (StructureGraphNode child : getOrderedChildren()) {
				if (child.getCompartmentDescription().equals("Try main try node")) {
					
				}
				else if (child.getCompartmentDescription().contains("Try catch node #")) {
					catchCount++;
				}
			}
			
			// add in height of spacers between blocks
			for (StructureGraphNode child : this.getOrderedChildren()) {
				myHeight = myHeight + child.setOuterBoxDimensions(drawWidth - 50);
				((NassiShneidermanTryBlock) blockToDraw).setNumberCatches(catchCount - 50);
			}
			
			myHeight = myHeight + 50 * (2 + catchCount);
		}
		
		else if (blockToDraw == null) {
			for (StructureGraphNode child : this.getOrderedChildren()) {
				myHeight = myHeight + child.setOuterBoxDimensions(drawWidth);
			}
		}
		else if (blockToDraw instanceof NassiShneidermanExprBlock){
			// Top or bottom surround for while, etc.
			myHeight = myHeight + 50;
			
			for (StructureGraphNode child : this.getOrderedChildren()) {
				myHeight = myHeight + child.setOuterBoxDimensions(drawWidth);
			}
		}
		else if (blockToDraw instanceof NassiShneidermanForBlock){
			// Top or bottom surround for for, while, etc.
			myHeight = myHeight + 100;
			
			for (StructureGraphNode child : this.getOrderedChildren()) {
				myHeight = myHeight + child.setOuterBoxDimensions(drawWidth - 50);
			}
		}
		
		else if (blockToDraw instanceof NassiShneidermanWhileBlock){
			// Top or bottom surround for for, while, etc.
			myHeight = myHeight + 50;
			
			for (StructureGraphNode child : this.getOrderedChildren()) {
				myHeight = myHeight + child.setOuterBoxDimensions(drawWidth - 50);
			}
		}
		
		if (!(blockToDraw == null)) {
			blockToDraw.setBlockHeight(myHeight);
			blockToDraw.setBlockWidth(drawWidth);
		}
		
		heightSpy = myHeight;
		widthSpy = drawWidth;
		
		return myHeight;
	}
	
	public void setStartLine(Integer startLine) {
		nodeStartLine = startLine;
	}
	
	public void setEndLine(Integer endLine) {
		nodeEndLine = endLine;
	}
	
	public void setBlockToDraw(NassiShneidermanBlock setBlock) {
		blockToDraw = setBlock;
	}
	
	public void setCompartmentDescription(String desc) {
		compartmentDescription = desc;
	}
	
	public String toString() {
		if (blockToDraw != null) {
			return ("StructuredGraphNode[startLine:" + nodeStartLine + ", endLine:" +
					nodeEndLine + ", type:" + blockToDraw.toString() + "]");
		}
		else {
			return ("StructuredGraphNode[startLine:" + nodeStartLine + ", endLine:" +
					nodeEndLine + ", type:" + compartmentDescription + "]");
		}
	}
	
	public Integer getHeight() {
		return heightSpy;
	}
	
	public Integer getWidth() {
		return widthSpy;
	}
	
	// TODO: Block height and width are now set - need to modify method to set corner
	// locations to complete the drawing info
	
	public List<NassiShneidermanBlock> setInnerBoxDimensionsAndCollectDrawings(
			List<NassiShneidermanBlock> soFar,
			Integer methodOffset) {
		
		// Check to see if this is a node that needs drawing
		
		if (blockToDraw != null) {
			
			/* If this is a try block, need to adjust its compartment sizes intelligently
			 * using data already collected.
			 */
			
			if (blockToDraw instanceof NassiShneidermanTryBlock) {
				/* If the condition above is true, expect this to be a try base and thus have
				 * try and catch children
				 * 
				 */
				
				for (StructureGraphNode child : getOrderedChildren()) {
					if (child.getCompartmentDescription().equals("Try main try node")) {
						((NassiShneidermanTryBlock) blockToDraw).setTryBodyHeight(child.getHeight());
					}
					else if (child.getCompartmentDescription().contains("Try catch node #")) {
						Integer catchIndex = 
								Integer.decode(child.getCompartmentDescription().split("# ")[1]);
						
						((NassiShneidermanTryBlock) blockToDraw).setCatchHeight(child.getHeight(), catchIndex);
					}
				}
			}
			
			/* Need to calculate how much height is taken up by previous peers to adjust
			 * start Y location after getting location from parent's body box.
			 * 
			 */
			
			soFar.add(blockToDraw);
			
			Integer startX = 0;
			Integer startY = 0;
			
			Integer localYOffset = 0;
			
			Integer stopAtIndex = parentNode.getOrderedChildren().indexOf(this);
			
			for (int i = 0; i < stopAtIndex; i++) {
				StructureGraphNode nodeToGet = parentNode.getOrderedChildren().get(i);
				
				localYOffset = localYOffset + nodeToGet.getHeight();
			}
			
			// case where parent node is one of the if compartments
			
			if (parentNode.getCompartmentDescription().equals("If true side")) {
				startX = ((NassiShneidermanIfBlock) parentNode.parentNode.blockToDraw).getTrueSideStartX();
				startY = ((NassiShneidermanIfBlock) parentNode.parentNode.blockToDraw).getTrueSideStartY();
			}
			else if (parentNode.getCompartmentDescription().equals("If false side")) {
				startX = ((NassiShneidermanIfBlock) parentNode.parentNode.blockToDraw).getFalseSideStartX();
				startY = ((NassiShneidermanIfBlock) parentNode.parentNode.blockToDraw).getFalseSideStartY();
			}
			// case where parent node is try or one of the catch compartments
			
			else if (parentNode.getCompartmentDescription().equals("Try main try node")) {
				startX = ((NassiShneidermanTryBlock) parentNode.parentNode.blockToDraw).getBodyStartX();
				startY = ((NassiShneidermanTryBlock) parentNode.parentNode.blockToDraw).getBodyStartY();
			}
			
			else if (parentNode.getCompartmentDescription().contains("Try catch node #")) {
				Integer catchIndex = 
						Integer.decode(parentNode.getCompartmentDescription().split("# ")[1]);
				
				startX = ((NassiShneidermanTryBlock) parentNode.parentNode.blockToDraw).getCatchStartX(catchIndex);
				startY = ((NassiShneidermanTryBlock) parentNode.parentNode.blockToDraw).getCatchStartY(catchIndex);
			}
			// only apply big offsets here
			
			else if (parentNode.getCompartmentDescription().equals("Method Root")) {
				startY = methodOffset;
			}
			
			// other structure type that has just one body compartment
			
			else {
				if (parentNode.blockToDraw instanceof NassiShneidermanExprBlock) {
					startX = ((NassiShneidermanExprBlock) parentNode.blockToDraw).getBodyStartX();
					startY = ((NassiShneidermanExprBlock) parentNode.blockToDraw).getBodyStartY();
				}
				else if (parentNode.blockToDraw instanceof NassiShneidermanForBlock) {
					startX = ((NassiShneidermanForBlock) parentNode.blockToDraw).getBodyStartX();
					startY = ((NassiShneidermanForBlock) parentNode.blockToDraw).getBodyStartY();
				}
				else if (parentNode.blockToDraw instanceof NassiShneidermanWhileBlock) {
					startX = ((NassiShneidermanWhileBlock) parentNode.blockToDraw).getBodyStartX();
					startY = ((NassiShneidermanWhileBlock) parentNode.blockToDraw).getBodyStartY();
				}
			}
			
			blockToDraw.setBlockStartHorizontal(startX);
			blockToDraw.setBlockStartVertical(startY + localYOffset);
		
		}
		
		// traverse deeper into the graph and set up those nodes also
		
		for (StructureGraphNode child : getOrderedChildren()) {
			child.setInnerBoxDimensionsAndCollectDrawings(soFar, methodOffset);
		}
		
		String tester = "";
		
		return soFar;
	}
 }