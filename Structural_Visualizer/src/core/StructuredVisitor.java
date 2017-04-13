package core;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import graph.StructureGraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

// A visitor that collects up the structure of a given piece of code

// TODO: Should probably put in a helper for stuff that is common to start or end visits for all node types

public class StructuredVisitor extends ASTVisitor {
	
	Set names;
	// Starter cu to be used to get code line
	CompilationUnit cu;
	
	Integer visitCounter;
	Integer indentCounter;
	
	Map<ASTNode, Integer> startVisitNumber;
	Map<ASTNode, Integer> endVisitNumber;
	
	Map<ASTNode, ASTNode> nodeToDiscoveredParent;
	Map<ASTNode, Integer> childCount;
	
	SortedMap<Integer, ASTNode> startLineToNode;
	SortedMap<Integer, ASTNode> endLineToNode;
	
	Map<ASTNode, Integer> nodeToStartLine;
	Map<ASTNode, Integer> nodeToEndLine;
	
	// Map to support an ordered readout of structure blocks with their nesting level
	// relative to one another
	Map<ASTNode, Integer> indentLevel;
	
	List<ASTNode> encountered;
	
	public StructuredVisitor(CompilationUnit cuIn) {
		names = new HashSet();
		cu = cuIn;
		visitCounter = 1;
		
		indentCounter = 1;
		
		startVisitNumber = new HashMap<ASTNode, Integer>();
		endVisitNumber = new HashMap<ASTNode, Integer>();
		
		nodeToDiscoveredParent = new HashMap<ASTNode, ASTNode>();
		childCount = new HashMap<ASTNode, Integer>();
		
		startLineToNode = new TreeMap<Integer, ASTNode>();
		endLineToNode = new TreeMap<Integer, ASTNode>();
		
		nodeToStartLine = new HashMap<ASTNode, Integer>();
		nodeToEndLine = new HashMap<ASTNode, Integer>();
		
		indentLevel = new HashMap<ASTNode, Integer>();
		
		encountered = new ArrayList();
	}
	
	@Override
	public boolean visit(Assignment node) {
		System.out.println(" Found assignment expression " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++; */
		return false; // do not move into children 
	}
	
	@Override
	public void endVisit(Assignment node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(DoStatement node) {
		System.out.println(" Found do statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++; */
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(DoStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	
	
	@Override
	public boolean visit(MethodDeclaration node) {
		System.out.println(" Found method declaration " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(MethodDeclaration node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		System.out.println(" Found method invocation " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++; */
		return false; // do not move into children 
	}
	
	@Override
	public void endVisit(MethodInvocation node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
		System.out.println(" Found for statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/ 
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(EnhancedForStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(ForStatement node) {
		System.out.println(" Found for statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(ForStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(IfStatement node) {
		System.out.println(" Found if statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(IfStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(SwitchStatement node) {
		System.out.println(" Found switch statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(SwitchStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	/*
	@Override
	public boolean visit(ThrowStatement node) {
		System.out.println(" Found throw statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(ThrowStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	*/
	
	@Override
	public boolean visit(TryStatement node) {
		System.out.println(" Found try statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(TryStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		System.out.println(" Found variable declaration " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++; */
		return false; // do not move into children 
	}
	
	@Override
	public void endVisit(VariableDeclarationExpression node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	@Override
	public boolean visit(WhileStatement node) {
		System.out.println(" Found while statement " + "at line"
					+ cu.getLineNumber(node.getStartPosition()));
		
		commonStartVisitHelper(node);
		/*
		startVisitNumber.put(node, visitCounter);
		indentLevel.put(node, indentCounter);
		lineToNode.put(cu.getLineNumber(node.getStartPosition()), node);
		encountered.add(node);
		visitCounter++;
		indentCounter++;
		*/
		return true; // Move into children 
	}
	
	@Override
	public void endVisit(WhileStatement node) {
		endVisitNumber.put(node, visitCounter);
		visitCounter++;
		indentCounter--;
		return;
	}
	
	public void reportVisitNumber() {
		for (ASTNode anode : encountered) {
			//System.out.println(" Start visit number for " + anode.getClass().getName() + "@" +
			//anode.hashCode() + " is " +
			//	startVisitNumber.get(anode));
			//System.out.println(" End visit number for " + anode.getClass().getName() + " is " +
			//		endVisitNumber.get(anode));
			
			System.out.println(" Indent level for " + anode.getClass().getName() + "@" +
					anode.hashCode() + " is " +
					indentLevel.get(anode));
		}
		
	}
	
	public List<ASTNode> getEncountered() {
		return encountered;
	}
	
	public Map<ASTNode, Integer> getIndentLevel(){
		return indentLevel;
	}
	
	public Map<ASTNode, ASTNode> getNodeToDiscoveredParent() {
		return nodeToDiscoveredParent;
	}
	
	public Map<ASTNode, Integer> getChildCount() {
		return childCount;
	}
	
	public Integer getStartLine(ASTNode toCheck) {
		return nodeToStartLine.get(toCheck);
	}
	
	public Integer getEndLine(ASTNode toCheck) {
		return nodeToEndLine.get(toCheck);
	}
	
	// A helper to do common actions amongst start visits
	
	private void commonStartVisitHelper(ASTNode foundNode) {
		startVisitNumber.put(foundNode, visitCounter);
		indentLevel.put(foundNode, indentCounter);
		startLineToNode.put(cu.getLineNumber(foundNode.getStartPosition()), foundNode);
		endLineToNode.put(cu.getLineNumber(foundNode.getStartPosition() +
				foundNode.getLength()), foundNode);
		
		nodeToStartLine.put(foundNode, cu.getLineNumber(foundNode.getStartPosition()));
		
		nodeToEndLine.put(foundNode, cu.getLineNumber(foundNode.getStartPosition() +
				foundNode.getLength()));
		
		// go backward on the encountered list until you find something lower than you
		
		if (indentCounter > 1) {
		
			for (int i = encountered.size() - 1; i >= 0; i--) {
				ASTNode checkNode = encountered.get(i);
				if (indentCounter > indentLevel.get(checkNode)) {
					nodeToDiscoveredParent.put(foundNode, checkNode);
					if (childCount.get(checkNode) != null) {
						Integer previousCount = childCount.get(checkNode);
						childCount.put(checkNode, previousCount + 1);
					}
					else {
						childCount.put(checkNode, 1);
					}
					break;
				}
				
			}
		}
		
		encountered.add(foundNode);
		visitCounter++;
		indentCounter++;
	}
	
	public ASTNode getNodeFromLine(Integer line) {
		
		SortedMap<Integer, ASTNode> beforeLine = startLineToNode.headMap(line);
		
		Integer startPinch = beforeLine.lastKey();
		ASTNode startNode = beforeLine.get(startPinch);
		
		Integer endPinch = cu.getLineNumber(startNode.getStartPosition() +
				startNode.getLength());
		
		if (startNode instanceof IfStatement){
		
			Boolean onTrueSide = true;
			// figure out if it is on the true side or false side
			Statement elseStatement = ((IfStatement) startNode).getElseStatement();
			
			if (elseStatement != null) {
				if (elseStatement.getStartPosition() - startNode.getStartPosition() <= 0) {
					onTrueSide = false;
				}
			}
		}
		
		return beforeLine.get(startPinch);
	}
}