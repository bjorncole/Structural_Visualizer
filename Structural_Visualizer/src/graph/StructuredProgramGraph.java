package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.WhileStatement;

import core.NassiShneidermanBlock;
import core.NassiShneidermanExprBlock;
import core.NassiShneidermanForBlock;
import core.NassiShneidermanIfBlock;
import core.NassiShneidermanTryBlock;
import core.NassiShneidermanWhileBlock;
import core.StructuredVisitor;

/*** A graph structure to capture dependencies between drawn nodes to help have an
 * intelligent layout of the drawings.
 * 
 * @author Bjorn
 *
 */

public class StructuredProgramGraph {
	
	private List<StructureGraphNode> nodesToDiagram;
	
	private Map<ASTNode, StructureGraphNode> nodeToGraphNode;
	
	private StructureGraphNode rootNode;
	
	public StructuredProgramGraph(StructuredVisitor populatedVisitor) {
		nodesToDiagram = new ArrayList<StructureGraphNode>();
		nodeToGraphNode = new HashMap<ASTNode, StructureGraphNode>();
		
		rootNode = new StructureGraphNode();
		
		constructorHelper(populatedVisitor);
	}

	public void constructorHelper(StructuredVisitor structureHolder) {
		/* Go over nodes in sequence and build a navigation tree from them
		 * that accounts for the compartments of the eventual diagram.
		 * 
		 */
		
		for (ASTNode sequencedNode : structureHolder.getEncountered()) {
			NassiShneidermanBlock newBlock = null;
			
			StructureGraphNode parentNode = navigateToParentGraphNode(
					structureHolder, sequencedNode);
			
			if (sequencedNode instanceof MethodDeclaration) {
				StructureGraphNode methodRootNode = new StructureGraphNode();
				
				methodRootNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				methodRootNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				methodRootNode.setCompartmentDescription("Method Root");
				
				nodeToGraphNode.put(sequencedNode, methodRootNode);
				
				rootNode.addChild(methodRootNode);
			}
			
			else if (sequencedNode instanceof IfStatement) {
				
				StructureGraphNode ifBaseNode = new StructureGraphNode();
				StructureGraphNode trueNode = new StructureGraphNode();
				StructureGraphNode falseNode = new StructureGraphNode();
				
				ifBaseNode.setBlockToDraw(new NassiShneidermanIfBlock());
				
				// let the if statement have a true and a false to work off of
				
				ifBaseNode.addChild(trueNode);
				ifBaseNode.addChild(falseNode);
				
				trueNode.setCompartmentDescription("If true side");
				falseNode.setCompartmentDescription("If false side");
				
				ifBaseNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				ifBaseNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				nodeToGraphNode.put(sequencedNode, ifBaseNode);
				
				nodesToDiagram.add(ifBaseNode);
				
				parentNode.addChild(ifBaseNode);
			}
			
			else if (sequencedNode instanceof ForStatement ||
					sequencedNode instanceof EnhancedForStatement) {
				
				StructureGraphNode forBaseNode = new StructureGraphNode();
				
				forBaseNode.setBlockToDraw(new NassiShneidermanForBlock());
				
				forBaseNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				forBaseNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				nodeToGraphNode.put(sequencedNode, forBaseNode);
				
				nodesToDiagram.add(forBaseNode);
				
				parentNode.addChild(forBaseNode);
			}
			
			else if (sequencedNode instanceof WhileStatement) {
				
				StructureGraphNode whileBaseNode = new StructureGraphNode();
				
				whileBaseNode.setBlockToDraw(new NassiShneidermanWhileBlock());
				
				whileBaseNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				whileBaseNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				nodeToGraphNode.put(sequencedNode, whileBaseNode);
				
				nodesToDiagram.add(whileBaseNode);
				
				parentNode.addChild(whileBaseNode);
			}
			
			else if (sequencedNode instanceof TryStatement) {
				
				StructureGraphNode tryBaseNode = new StructureGraphNode();
				
				tryBaseNode.setBlockToDraw(new NassiShneidermanTryBlock());
				
				tryBaseNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				tryBaseNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				StructureGraphNode tryNode = new StructureGraphNode();
				tryNode.setCompartmentDescription("Try main try node");
				tryBaseNode.addChild(tryNode);
				
				Integer numberOfCatches = ((TryStatement) sequencedNode).catchClauses().size();
				
				for (int i = 0; i < numberOfCatches; i++) {
					StructureGraphNode catchNode = new StructureGraphNode();
					catchNode.setCompartmentDescription("Try catch node # " + (i + 1));
					tryBaseNode.addChild(catchNode);
				}
				
				nodeToGraphNode.put(sequencedNode, tryBaseNode);
				
				nodesToDiagram.add(tryBaseNode);
				
				parentNode.addChild(tryBaseNode);
			}
			
			else if (sequencedNode instanceof Assignment || 
					sequencedNode instanceof MethodInvocation ||
					sequencedNode instanceof VariableDeclarationExpression) {
				
				StructureGraphNode exprBaseNode = new StructureGraphNode();
				
				exprBaseNode.setBlockToDraw(new NassiShneidermanExprBlock());
				
				exprBaseNode.setStartLine(structureHolder.getStartLine(sequencedNode));
				exprBaseNode.setEndLine(structureHolder.getEndLine(sequencedNode));
				
				nodeToGraphNode.put(sequencedNode, exprBaseNode);
				
				nodesToDiagram.add(exprBaseNode);
				
				parentNode.addChild(exprBaseNode);
			}
			
		}
	}
	
	public StructureGraphNode navigateToParentGraphNode(
			StructuredVisitor structureHolder,
			ASTNode sequencedNode) {
		
		ASTNode parentASTNode = structureHolder.
				getNodeToDiscoveredParent().get(sequencedNode);
		
		StructureGraphNode parentNode = nodeToGraphNode.get(parentASTNode);
		
		// check which side the node is on
		
		if (parentASTNode instanceof IfStatement) {
			Boolean onTrueSide = true;
			
			// figure out if it is on the true side or false side
			Statement elseStatement = ((IfStatement) parentASTNode).getElseStatement();
			
			if (elseStatement != null) {
				if (elseStatement.getStartPosition() - sequencedNode.getStartPosition() <= 0) {
					onTrueSide = false;
				}
			}
			
			if (onTrueSide) {
				for (StructureGraphNode testNode : parentNode.getOrderedChildren()) {
					if (testNode.getCompartmentDescription().equals("If true side")) {
						parentNode = testNode;
					}
				}
			}
			else {
				for (StructureGraphNode testNode : parentNode.getOrderedChildren()) {
					if (testNode.getCompartmentDescription().equals("If false side")) {
						parentNode = testNode;
					}
				}
			}
			
		}
		
		else if (parentASTNode instanceof TryStatement) {
			// figure out if it is try or which catch
			Boolean inTry = true;
			
			Integer catchIndex = 0;
			Integer foundCatch = 0;
			
			for (Object testedCatch : ((TryStatement) parentASTNode).catchClauses()) {
				
				if (testedCatch instanceof CatchClause) {
					CatchClause workingCatch = (CatchClause) testedCatch;
					
					if (workingCatch.getStartPosition() - sequencedNode.getStartPosition() <= 0) {
						inTry = false;
						foundCatch = catchIndex;
					}
					
				}
				
				catchIndex++;
			}
			
			if (inTry) {
				for (StructureGraphNode testNode : parentNode.getOrderedChildren()) {
					if (testNode.getCompartmentDescription().equals("Try main try node")) {
						parentNode = testNode;
					}
				}
			}
			else {
				for (StructureGraphNode testNode : parentNode.getOrderedChildren()) {
					if (testNode.getCompartmentDescription().equals(
							"Try catch node # " + (foundCatch + 1))) {
						parentNode = testNode;
					}
				}
			}
			
		}
		
		return parentNode;
	}
	
	public List<StructureGraphNode> getNodesToDiagram() {
		return nodesToDiagram;
	}
	
	public StructureGraphNode getRootNode() {
		return rootNode;
	}
	
}