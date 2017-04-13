package core;

import graph.StructureGraphNode;
import graph.StructuredProgramGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

class StructuredProgram {
	
	private ASTNode topNode;
	
	// Retainer of results of visiting the AST with a structural focus
	private StructuredVisitor structureHolder;
	
	private StructuredProgramGraph structureGraph;
	
	// TODO: Decide whether the NS container should be for just one method or multiple (I'm thinking just the one per method at a time)
	private List<NassiShneiderman> nsList;
	
	private Map<ASTNode, NassiShneidermanBlock> nodeToDrawing;
	
	private Map<ASTNode, Integer> workingCounter;
	
	/*** Constructor with source file as a block of text in a string.
	 *   Will parse source text and extract structural constructs with
	 *   start visit and end visit times in order to support reporting
	 *   on general control flow structure of the code.
	***/
	
	public StructuredProgram(String sourceText) {
		
		nodeToDrawing = new HashMap<ASTNode, NassiShneidermanBlock>();
		
		nsList = new ArrayList<NassiShneiderman>();
		
		workingCounter = new HashMap<ASTNode, Integer>();
		
		/* Parse the source using a recipe for the Eclipse JDT and a customized
		 * ASTVistor than focuses on control flow constructs in code (the "structure" in
		 * "structured programming")
		 */
		
		// handles JDK up to 1.8
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);  
		parser.setSource(sourceText.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		Integer counter = 0;
		
		// TODO: What does this setting really do?
		// In order to parse 1.6 code, some compiler options need to be set to 1.6
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);
		parser.setCompilerOptions(options);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		structureHolder = new StructuredVisitor(cu);
		
		cu.accept(structureHolder);
		
		structureHolder.reportVisitNumber();
		
		ASTNode previouslyEncountered = null;
		
		topNode = cu;
		
		// Generate a graph of nodes to draw from the syntax tree sampling
		structureGraph = new StructuredProgramGraph(structureHolder);
		
		arrangeDiagrams();
	}
	
	/*** Helper method to get various diagram blocks together
	 * 
	 */
	
	private void arrangeDiagrams() {
		Integer totalGraphWidth = 1600;
		
		NassiShneiderman nsCurrent = null;
		
		Integer previousHeight = 0;
		
		for (StructureGraphNode methodStart : structureGraph.getRootNode().getOrderedChildren()) {
			if (methodStart.getCompartmentDescription().equals("Method Root")) {
				
				if (nsCurrent != null) {
					nsList.add(nsCurrent);
				}
				
				nsCurrent = new NassiShneiderman();
				
				/* Need to determine the height at which to draw the container */
				
				Integer methodHeight = methodStart.setOuterBoxDimensions(totalGraphWidth);
				
				List<NassiShneidermanBlock> methodDrawings =
					methodStart.setInnerBoxDimensionsAndCollectDrawings(
							new ArrayList<NassiShneidermanBlock>(), previousHeight);
				
				/* Since drawing graph nodes link to the appropriate NSBlock, need to set their
				 * drawing parameters correctly
				 */
				
				//methodStart.setDrawnBlockParameters(totalGraphWidth);
				
				previousHeight = previousHeight + methodHeight + 50;
				
				
				
				String tester = "";
				tester = tester + "";
				/* When individual block parameters are set - need to aggregate them into
				 * an NS diagram so that the calls to draw it can be done correctly.
				 * 
				 * This will cause NS blocks to be linked to both the diagram they are in
				 * as well as the Structured Graph.
				 */
				
				for (NassiShneidermanBlock drawnBlock : methodDrawings) {
					nsCurrent.addBlock(drawnBlock);
				}
			}
			else {
				/* Ignore - the node represents field declarations. */
			}
		}
		
		// add last diagram to the set
		
		nsList.add(nsCurrent);
	}
	
	/*
	
	private void arrangeDiagrams() {
		
		// can use block lines to understand how tall a segment needs to be
		
		NassiShneiderman nsCurrent = null;
		Integer currentPosition = 0;
		
		Integer methodChildren = 0;
		
		for (ASTNode sequencedNode : structureHolder.getEncountered()) {
			
			NassiShneidermanBlock newBlock = null;
			
			if (sequencedNode instanceof MethodDeclaration) {
				// start up a new diagram
				if (nsCurrent != null) {
					nsList.add(nsCurrent);
				}
				
				currentPosition = currentPosition + 100;
				
				nsCurrent = new NassiShneiderman();
				
				methodChildren = structureHolder.getChildCount().get(sequencedNode);
				
				methodChildren = getSubChildrenCount(sequencedNode);
				
				if (methodChildren == null) {
					methodChildren = 0;
				}
			}
			Integer startLine = ((CompilationUnit) topNode).getLineNumber(sequencedNode.getStartPosition());
			Integer endLine = ((CompilationUnit) topNode).getLineNumber(sequencedNode.getLength() + sequencedNode.getStartPosition());
			
			Integer childLines = 1;
			
			if (structureHolder.getChildCount().get(sequencedNode) != null) {
				childLines = structureHolder.getChildCount().get(sequencedNode);
			}
			
			Integer boxHeight = Math.max((methodChildren) * 90, 150);
			
			Integer totalWidth = 1200;
			
			if (sequencedNode instanceof IfStatement) {
				newBlock = new NassiShneidermanIfBlock();
				
				// need to work out how to get running vertical position
				
				// allocate 80 to header, 50 to subheader, then use remainder for rest
				
				//newBlock.setBlockHeight(boxHeight);
				newBlock.setHeaderHeight(60);
				newBlock.setSubheaderHeight(30);
				
				newBlock.setHeaderWidth(totalWidth);
				newBlock.setSubheaderWidth(totalWidth);
				
				nsCurrent.addBlock(newBlock);
			
			}
			else if (sequencedNode instanceof ForStatement || sequencedNode instanceof EnhancedForStatement) {
				newBlock = new NassiShneidermanForBlock();
				
				// need to work out how to get running vertical position
				
				// allocate 80 to header, 50 to subheader, then use remainder for rest
				
				//newBlock.setBlockHeight(boxHeight);
				newBlock.setHeaderHeight(40);
				newBlock.setSubheaderHeight(40);
				
				newBlock.setBlockWidth(totalWidth);
				
				nsCurrent.addBlock(newBlock);
			}
			
			else if (sequencedNode instanceof WhileStatement) {
				newBlock = new NassiShneidermanWhileBlock();
				
				// need to work out how to get running vertical position
				
				// allocate 80 to header, 50 to subheader, then use remainder for rest
				
				//newBlock.setBlockHeight(boxHeight);
				newBlock.setHeaderHeight(40);
				newBlock.setSubheaderHeight(40);
				
				newBlock.setBlockWidth(totalWidth);
				
				nsCurrent.addBlock(newBlock);
			}
			
			else if (sequencedNode instanceof TryStatement) {
				newBlock = new NassiShneidermanTryBlock();
				
				Integer numberOfCatches = ((TryStatement) sequencedNode).catchClauses().size();
				
				// need to work out how to get running vertical position
				
				((NassiShneidermanTryBlock) newBlock).setNumberOfCatches(numberOfCatches);
				
				for (int i = 0; i < numberOfCatches; i++) {
					((NassiShneidermanTryBlock) newBlock).setCatchHeight(40, i);
				}
				
				// allocate 80 to header, 50 to subheader, then use remainder for rest
				
				newBlock.setHeaderHeight(40);
				newBlock.setSubheaderHeight(40);
				
				newBlock.setBlockWidth(totalWidth);
				
				nsCurrent.addBlock(newBlock);
			}
			
			else if (sequencedNode instanceof Assignment || 
					sequencedNode instanceof MethodInvocation ||
					sequencedNode instanceof VariableDeclarationExpression) {
				newBlock = new NassiShneidermanExprBlock();
				
				// allocate 80 to header, 50 to subheader, then use remainder for rest
				
				newBlock.setHeaderHeight(40);
				newBlock.setSubheaderHeight(40);
				
				newBlock.setBlockWidth(totalWidth);
				
				nsCurrent.addBlock(newBlock);
			}
			
			// then determine if box needs to go in an "if" column, and if so, which
			
			// TODO: Get workingcounter and total height working off children of local node
			
			if (sequencedNode != null && newBlock != null) {
				
				newBlock.setBlockHeight(boxHeight);
				
				ASTNode parentTest = structureHolder.getNodeToDiscoveredParent().get(sequencedNode);
				
				// TODO: If there are no children, no need to do anything
				
				// TODO: If there are children, need to know if there are expressions before or after children (or between?)
				
				Double newBlockChild = getSubChildrenCount(sequencedNode).doubleValue() + 1.0;
				
				Double parentChild = getSubChildrenCount(parentTest).doubleValue();
				
				if (workingCounter.get(parentTest) == null) {
					workingCounter.put(parentTest, 0);
				}
			
				if (parentTest instanceof IfStatement) {
					Boolean onTrueSide = true;
					// figure out if it is on the true side or false side
					Statement elseStatement = ((IfStatement) parentTest).getElseStatement();
					
					if (elseStatement != null) {
						if (elseStatement.getStartPosition() - sequencedNode.getStartPosition() <= 0) {
							onTrueSide = false;
						}
					}
					
					// find the place to locate the upper-left corner of this block - for now assume true side
					
					Integer startX = 0;
					Integer startY = 0;
					
					if (onTrueSide) {
						startX = ((NassiShneidermanIfBlock) nodeToDrawing.get(
								structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getTrueSideStartX();
						startY = ((NassiShneidermanIfBlock) nodeToDrawing.get(
								structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getTrueSideStartY();
					} else {
						startX = ((NassiShneidermanIfBlock) nodeToDrawing.get(
								structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getFalseSideStartX();
						startY = ((NassiShneidermanIfBlock) nodeToDrawing.get(
								structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getFalseSideStartY();
					}
					
					Integer columnWidth = ((NassiShneidermanIfBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getColumnWidth();
					
					Integer columnHeight = ((NassiShneidermanIfBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getColumnHeight();
					
					newBlock.setBlockStartHorizontal(startX);
					//newBlock.setBlockStartVertical(startY + (columnHeight / columnDivisor) * columnOffset);
					
					Double newHeight = columnHeight * (newBlockChild / parentChild);
					Double newOffset = columnHeight * (workingCounter.get(parentTest).doubleValue() / parentChild);
					
					newBlock.setBlockStartVertical(startY + newOffset.intValue());
					
					newBlock.setBlockHeight(newHeight.intValue());
					
					newBlock.setBlockWidth(columnWidth);
					//newBlock.setBlockHeight(columnHeight / columnDivisor);
					
				}
				
				else if (parentTest instanceof ForStatement || parentTest instanceof EnhancedForStatement) {
					// figure out if it is on the true side or false side
					
					// find the place to locate the upper-left corner of this block - for now assume true side
					
					Integer startX = ((NassiShneidermanForBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartX();
					Integer startY = ((NassiShneidermanForBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartY();
					
					Integer columnWidth = ((NassiShneidermanForBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyWidth();
					
					Integer columnHeight = ((NassiShneidermanForBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyHeight();
					
					newBlock.setBlockStartHorizontal(startX);
					//newBlock.setBlockStartVertical(startY + (columnHeight / columnDivisor) * columnOffset);
					
					Double newHeight = columnHeight * (newBlockChild / parentChild);
					Double newOffset = columnHeight * (workingCounter.get(parentTest).doubleValue() / parentChild);
					
					newBlock.setBlockStartVertical(startY + newOffset.intValue());
					
					newBlock.setBlockHeight(newHeight.intValue());
					
					newBlock.setBlockWidth(columnWidth);
					//newBlock.setBlockHeight(columnHeight / columnDivisor);
					
				}
				
				else if (parentTest instanceof WhileStatement) {
					// figure out if it is on the true side or false side
					
					// find the place to locate the upper-left corner of this block - for now assume true side
					
					Integer startX = ((NassiShneidermanWhileBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartX();
					Integer startY = ((NassiShneidermanWhileBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartY();
					
					Integer bodyWidth = ((NassiShneidermanWhileBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyWidth();
					
					Integer columnHeight = ((NassiShneidermanWhileBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyHeight();
					
					newBlock.setBlockStartHorizontal(startX);
					
					Double newHeight = columnHeight * (newBlockChild / parentChild);
					Double newOffset = columnHeight * (workingCounter.get(parentTest).doubleValue() / parentChild);
					
					newBlock.setBlockStartVertical(startY + newOffset.intValue());
					
					newBlock.setBlockHeight(newHeight.intValue());
					
					newBlock.setBlockWidth(bodyWidth);
					
				}
				
				else if (parentTest instanceof TryStatement) {
					// figure out if it is on the true side or false side
					
					// find the place to locate the upper-left corner of this block - for now assume true side
					
					Integer startX = ((NassiShneidermanTryBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartX();
					Integer startY = ((NassiShneidermanTryBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyStartY();
					
					Integer bodyWidth = ((NassiShneidermanTryBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyWidth();
					
					Integer columnHeight = ((NassiShneidermanTryBlock) nodeToDrawing.get(
							structureHolder.getNodeToDiscoveredParent().get(sequencedNode))).getBodyHeight();
					
					newBlock.setBlockStartHorizontal(startX);

					Double newHeight = columnHeight * (newBlockChild / parentChild);
					Double newOffset = columnHeight * (workingCounter.get(parentTest).doubleValue() / parentChild);
					
					newBlock.setBlockStartVertical(startY + newOffset.intValue());
					
					newBlock.setBlockHeight(newHeight.intValue());
					
					newBlock.setBlockWidth(bodyWidth);
					
				}
				// not nested, just advance the reel
				
				else if (parentTest instanceof MethodDeclaration) {
					newBlock.setBlockStartVertical(currentPosition);
					
					currentPosition = currentPosition + boxHeight;
				}
				
				// finally, keep a reference between the new node and its drawing for reference
				
				if (newBlock != null) {
					nodeToDrawing.put(sequencedNode, newBlock);
				}
				
				
				workingCounter.put(parentTest, workingCounter.get(parentTest) + newBlockChild.intValue());
				
			}
		}
		
		nsList.add(nsCurrent);
		
		// indents can be decided individually
		
		// just need to look up to understand what side of if / else to put things on
		
		
	}
	
	*/
	
	/*** Method for getting the AST nodes for discovered methods
	 * 
	 * @return A list of methods found in this source file.
	 */
	/*
	public List<ASTNode> getMethodRoots() {
		
		ArrayList<ASTNode> methodsFound = new ArrayList<ASTNode>();
		
		for (ASTNode methodCheck : structureHolder.encountered) {
			if (methodCheck instanceof MethodDeclaration) {
				methodsFound.add(methodCheck);
			}
			
		}
		
		return methodsFound;
	}
	
	public List<NassiShneidermanForBlock> getNSFors() {
		
		List<NassiShneidermanForBlock> forDrawings = new ArrayList<NassiShneidermanForBlock>();
		
		NassiShneidermanForBlock forBlock1 = new NassiShneidermanForBlock();
		
		forBlock1.setHeaderHeight(70);
		forBlock1.setHeaderWidth(600);
		
		forBlock1.setSubheaderHeight(50);
		forBlock1.setSubheaderWidth(600);
		
		forBlock1.setBlockStartHorizontal(10);
		forBlock1.setBlockStartVertical(250);
		
		forBlock1.setBlockWidth(530);
		forBlock1.setBlockHeight(300);
		
		forBlock1.setHighlight("Body");
		
		forDrawings.add(forBlock1);
		
		return forDrawings;
	}
	
	// TODO: Move this over to other logic for generating blocks from input code and make it send ALL if's (for all methods found)
	
	public List<NassiShneidermanIfBlock> getNSIfs() {
		
		List<NassiShneidermanIfBlock> ifDrawings = new ArrayList<NassiShneidermanIfBlock>();
		
		NassiShneidermanIfBlock ifBlock1 = new NassiShneidermanIfBlock();
		
		ifBlock1.setHeaderHeight(70);
		ifBlock1.setHeaderWidth(600);
		
		ifBlock1.setSubheaderHeight(50);
		ifBlock1.setSubheaderWidth(600);
		
		ifBlock1.setBlockStartHorizontal(10);
		ifBlock1.setBlockStartVertical(10);
		
		ifBlock1.setHighlight("False");
		
		ifDrawings.add(ifBlock1);
		
		return ifDrawings;
	}
	*/
	public List<NassiShneiderman> getDiagrams() {
		return nsList;
	}
	
	private Integer getSubChildrenCount(ASTNode currentNode) {
		// recurse until no children found - otherwise go further and get children further down
		
		Integer localChildrenCount = structureHolder.getChildCount().get(currentNode);
		
		if (localChildrenCount == null) {
			return 0;
		}
		else {
			for (ASTNode potentialChild : structureHolder.getNodeToDiscoveredParent().keySet()) {
				// if this is the parent of a potential child, add its children to the total
				if (structureHolder.getNodeToDiscoveredParent().get(potentialChild) == currentNode) {
					localChildrenCount = localChildrenCount + getSubChildrenCount(potentialChild);
				}
			}
			return localChildrenCount;
		}
	}
	
	public void setHighlight(Integer line) {
		ASTNode figuredNode = structureHolder.getNodeFromLine(line);
		
	}
	
}