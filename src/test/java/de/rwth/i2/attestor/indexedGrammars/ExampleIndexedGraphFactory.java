package de.rwth.i2.attestor.indexedGrammars;

import java.util.ArrayList;
import java.util.List;

import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.HeapConfigurationBuilder;
import de.rwth.i2.attestor.graph.heap.internal.InternalHeapConfiguration;
import de.rwth.i2.attestor.indexedGrammars.stack.*;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.TypeFactory;
import gnu.trove.list.array.TIntArrayList;

public class ExampleIndexedGraphFactory {

	public static HeapConfiguration getBalancedTreeLeft3(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		HeapConfigurationBuilder builder = hc.builder();

		ConcreteStackSymbol s =  ConcreteStackSymbol.getStackSymbol("s", false);
		ConcreteStackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);

		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(s);
		leftStack.add(s);
		leftStack.add(s);
		leftStack.add(bottom);

		IndexedNonterminal leftNonterminal = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, leftStack);
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add(s);
		rightStack.add(s);
		rightStack.add(bottom);
		IndexedNonterminal rightNonterminal = new IndexedNonterminalImpl("B", rightStack );

		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "+1");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "-1");

		Type nodeType = TypeFactory.getInstance().getType("tree");

		TIntArrayList nodes = new TIntArrayList();
		builder.addNodes(nodeType, 4, nodes)
		.addSelector(nodes.get(0), left, nodes.get(1))
		.addSelector(nodes.get(0), right, nodes.get(2))
		.addNonterminalEdge(leftNonterminal, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
		.addNonterminalEdge(rightNonterminal, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
		.addVariableEdge("left", nodes.get(1))
		.addVariableEdge("right", nodes.get(2));

		return builder.build();
	}

	public static HeapConfiguration getCannotAbstractStack(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		HeapConfigurationBuilder builder = hc.builder();

		ConcreteStackSymbol s =  ConcreteStackSymbol.getStackSymbol("s", false);
		ConcreteStackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(s);
		leftStack.add(s);
		leftStack.add(s);
		leftStack.add(abs);

		IndexedNonterminal leftNonterminal = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, leftStack);
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add(s);
		rightStack.add(s);
		rightStack.add(bottom);
		IndexedNonterminal rightNonterminal = new IndexedNonterminalImpl("B", rightStack );

		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "+1");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "-1");

		Type nodeType = TypeFactory.getInstance().getType("tree");

		TIntArrayList nodes = new TIntArrayList();
		builder.addNodes(nodeType, 4, nodes)
		.addSelector(nodes.get(0), left, nodes.get(1))
		.addSelector(nodes.get(0), right, nodes.get(2))
		.addNonterminalEdge(leftNonterminal, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
		.addNonterminalEdge(rightNonterminal, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
		.addVariableEdge("left", nodes.get(1))
		.addVariableEdge("right", nodes.get(2));

		return builder.build();
	}
	
	public static HeapConfiguration getInput_MaterializeSmall_Z(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 2, nodes)
						.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1)}))
						.addVariableEdge("x", nodes.get(0))
						.build();
		
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeSmall_Z(){
		AnnotatedSelectorLabel leftLabel = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightLabel = new AnnotatedSelectorLabel("right", "0");
		
		HeapConfiguration res = new InternalHeapConfiguration();
		TIntArrayList nodes = new TIntArrayList();
		res = res.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 2, nodes)
						.addSelector(nodes.get(0), leftLabel, nodes.get(1))
						.addSelector(nodes.get(0), rightLabel, nodes.get(1))
						.addVariableEdge("x", nodes.get(0))
						.build();
		
		return res;
	}
	
	public static HeapConfiguration getInput_MaterializeSmall_sZ(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false);
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(s);
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 2, nodes)
						 .addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1)}))
						 .addVariableEdge("x", nodes.get(0))
						 .build();
		
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeSmall2_Res1(){
		HeapConfiguration hc = new InternalHeapConfiguration();
	
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", stack);
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "1");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "-1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 3, nodes)
				.addSelector(nodes.get(0), left, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), right, nodes.get(2))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(2)}))
				.addVariableEdge("x", nodes.get(0))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeSmall2_Res2(){
		HeapConfiguration hc = new InternalHeapConfiguration();
	
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", stack);
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 3, nodes)
				.addSelector(nodes.get(0), left, nodes.get(2))
				.addSelector(nodes.get(0), right, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(2)}))
				.addVariableEdge("x", nodes.get(0))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeSmall2_Res3(){
		HeapConfiguration hc = new InternalHeapConfiguration();
	
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stackL = new ArrayList<>();
		stackL.add(bottom);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", stackL);
		ArrayList<StackSymbol> stackR = new ArrayList<>();
		stackR.add(bottom);
		IndexedNonterminal rightNt = new IndexedNonterminalImpl("B", stackR);
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(TypeFactory.getInstance().getType("AVLTree"), 4, nodes)
				.addSelector(nodes.get(0), left, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), right, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
				.addVariableEdge("x", nodes.get(0))
				.addNonterminalEdge(rightNt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getInput_MaterializeBig(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false);
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(abs);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add(s);
		rightStack.add(abs);
		IndexedNonterminal rightNt = new IndexedNonterminalImpl("B", rightStack);
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), left, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), right, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
				.addNonterminalEdge(rightNt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeBig_Res1(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(bottom);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightAStack = new ArrayList<>();
		rightAStack.add(bottom);
		IndexedNonterminal rightANt = new IndexedNonterminalImpl("B", rightAStack);
		
		
		AnnotatedSelectorLabel leftM = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel rightP = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(type, 5, nodes)
				.addSelector(nodes.get(0), leftM, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightP, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addSelector(nodes.get(2), rightP, nodes.get(3))
				.addSelector(nodes.get(3), parent, nodes.get(2))
				.addSelector(nodes.get(2), leftM, nodes.get(4))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(4)}))
				.addNonterminalEdge(rightANt, new TIntArrayList(new int[]{nodes.get(3), nodes.get(4)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeBig_Res2(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(bottom);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightBStack = new ArrayList<>();
		rightBStack.add(bottom);
		IndexedNonterminal rightBNt = new IndexedNonterminalImpl("B", rightBStack);
		
		
		AnnotatedSelectorLabel leftM = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel leftP = new AnnotatedSelectorLabel("left", "1");
		AnnotatedSelectorLabel rightM = new AnnotatedSelectorLabel("right", "-1");
		AnnotatedSelectorLabel rightP = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		hc = hc.builder().addNodes(type, 5, nodes)
				.addSelector(nodes.get(0), leftM, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightP, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addSelector(nodes.get(2), rightM, nodes.get(4))
				.addSelector(nodes.get(2), leftP, nodes.get(3))
				.addSelector(nodes.get(3), parent, nodes.get(2))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(4)}))
				.addNonterminalEdge(rightBNt, new TIntArrayList(new int[]{nodes.get(3), nodes.get(4)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
		return hc;
	}
	
	public static HeapConfiguration getExpected_MaterializeBig_Res3(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(abs);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightAStack = new ArrayList<>();
		rightAStack.add(abs);
		IndexedNonterminal rightANt = new IndexedNonterminalImpl("B", rightAStack);
		ArrayList<StackSymbol> rightBStack = new ArrayList<>();
		rightBStack.add(abs);
		IndexedNonterminal rightBNt = new IndexedNonterminalImpl("B", rightBStack);
		
		
		AnnotatedSelectorLabel leftM = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel rightP = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		
		return hc.builder().addNodes(type, 6, nodes)
				.addSelector(nodes.get(0), leftM, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightP, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addSelector(nodes.get(2), leftZ, nodes.get(3))
				.addSelector(nodes.get(3), parent, nodes.get(2))
				.addSelector(nodes.get(2), rightZ, nodes.get(4))
				.addSelector(nodes.get(4), parent, nodes.get(2))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(5)}))
				.addNonterminalEdge(rightANt, new TIntArrayList(new int[]{nodes.get(3), nodes.get(5)}))
				.addNonterminalEdge(rightBNt, new TIntArrayList(new int[]{nodes.get(4), nodes.get(5)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
	}
	
	public static HeapConfiguration getExpected_MaterializeBig_Res4(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false);
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(s);
		leftStack.add(abs);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightAStack = new ArrayList<>();
		rightAStack.add(s);
		rightAStack.add(abs);
		IndexedNonterminal rightANt = new IndexedNonterminalImpl("B", rightAStack);
		ArrayList<StackSymbol> rightBStack = new ArrayList<>();
		rightBStack.add(abs);
		IndexedNonterminal rightBNt = new IndexedNonterminalImpl("B", rightBStack);
		
		
		AnnotatedSelectorLabel leftM = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel leftP = new AnnotatedSelectorLabel("left", "1");
		AnnotatedSelectorLabel rightP = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel rightM = new AnnotatedSelectorLabel("right", "-1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		
		return hc.builder().addNodes(type, 6, nodes)
				.addSelector(nodes.get(0), leftM, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightP, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addSelector(nodes.get(2), leftP, nodes.get(3))
				.addSelector(nodes.get(3), parent, nodes.get(2))
				.addSelector(nodes.get(2), rightM, nodes.get(4))
				.addSelector(nodes.get(4), parent, nodes.get(2))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(5)}))
				.addNonterminalEdge(rightANt, new TIntArrayList(new int[]{nodes.get(3), nodes.get(5)}))
				.addNonterminalEdge(rightBNt, new TIntArrayList(new int[]{nodes.get(4), nodes.get(5)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
	}
	
	public static HeapConfiguration getExpected_MaterializeBig_Res5(){
		HeapConfiguration hc = new InternalHeapConfiguration();
	
		StackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false);
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>();
		leftStack.add(s);
		leftStack.add(abs);
		IndexedNonterminal leftNt = new IndexedNonterminalImpl("B", 2, new boolean [] {false,true}, leftStack );
		ArrayList<StackSymbol> rightAStack = new ArrayList<>();
		rightAStack.add(abs);
		IndexedNonterminal rightANt = new IndexedNonterminalImpl("B", rightAStack);
		ArrayList<StackSymbol> rightBStack = new ArrayList<>();
		rightBStack.add(s);
		rightBStack.add(abs);
		IndexedNonterminal rightBNt = new IndexedNonterminalImpl("B", rightBStack);
		
		
		AnnotatedSelectorLabel leftM = new AnnotatedSelectorLabel("left", "-1");
		AnnotatedSelectorLabel rightP = new AnnotatedSelectorLabel("right", "1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		
		return hc.builder().addNodes(type, 6, nodes)
				.addSelector(nodes.get(0), leftM, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightP, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addSelector(nodes.get(2), leftM, nodes.get(3))
				.addSelector(nodes.get(3), parent, nodes.get(2))
				.addSelector(nodes.get(2), rightP, nodes.get(4))
				.addSelector(nodes.get(4), parent, nodes.get(2))
				.addNonterminalEdge(leftNt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(5)}))
				.addNonterminalEdge(rightANt, new TIntArrayList(new int[]{nodes.get(3), nodes.get(5)}))
				.addNonterminalEdge(rightBNt, new TIntArrayList(new int[]{nodes.get(4), nodes.get(5)}))
				.addVariableEdge("x", nodes.get(2))
				.build();
	}
	
	public static HeapConfiguration getInput_CanonizeSimple(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
			.addSelector(nodes.get(0), leftZ, nodes.get(1))
			.addSelector(nodes.get(0), rightZ, nodes.get(1))
			.build();
	}
	
	public static HeapConfiguration getExpected_CanonizeSimple(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add( AbstractStackSymbol.get("X"));
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1)}))
				.build();
	}
	
	public static HeapConfiguration getInput_CanonizeVar(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> stackLeft = new ArrayList<>();
		stackLeft.add(abs);
		IndexedNonterminal ntLeft = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stackLeft);
		ArrayList<StackSymbol> stackRight = new ArrayList<>();
		stackRight.add(abs);
		IndexedNonterminal ntRight = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stackRight);
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addNonterminalEdge(ntLeft, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
				.addNonterminalEdge(ntRight, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();		
	}
	
	public static HeapConfiguration getExpected_CanonizeVar(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		

		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(abs);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1)}))
				.build();
	}
	
	public static HeapConfiguration getInput_FieldAccess(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(1), leftZ, nodes.get(3))
				.addSelector(nodes.get(1), rightZ, nodes.get(3))
				.addVariableEdge("x", nodes.get(0))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();
	}
	
	public static HeapConfiguration getExpected_FieldAccess(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(1), leftZ, nodes.get(3))
				.addSelector(nodes.get(1), rightZ, nodes.get(3))
				.addVariableEdge("x", nodes.get(1))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();
	}
	
	public static HeapConfiguration getExpected_newNode(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 5, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(1), leftZ, nodes.get(3))
				.addSelector(nodes.get(1), rightZ, nodes.get(3))
				.addVariableEdge("x", nodes.get(1))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.addVariableEdge("tmp", nodes.get(4))
				.build();
	}
	
	public static HeapConfiguration getExpected_fieldAssign(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel leftU = new AnnotatedSelectorLabel("left", "");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		StackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 5, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(1), leftU, nodes.get(4))
				.addSelector(nodes.get(1), rightZ, nodes.get(3))
				.addVariableEdge("x", nodes.get(1))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.addVariableEdge("tmp", nodes.get(4))
				.build();
	}
	
	public static HeapConfiguration getInput_stackCanonization_longStack(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		ConcreteStackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false );
		ConcreteStackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		
		ArrayList<StackSymbol> stackLeft = new ArrayList<>();
		stackLeft.add(s);
		stackLeft.add(s);
		stackLeft.add(bottom);
		IndexedNonterminal ntLeft = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stackLeft);
		ArrayList<StackSymbol> stackRight = new ArrayList<>();
		stackRight.add(s);
		stackRight.add(s);
		stackRight.add(bottom);
		IndexedNonterminal ntRight = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stackRight);
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), leftZ, nodes.get(1))
				.addSelector(nodes.get(1), parent, nodes.get(0))
				.addSelector(nodes.get(0), rightZ, nodes.get(2))
				.addSelector(nodes.get(2), parent, nodes.get(0))
				.addNonterminalEdge(ntLeft, new TIntArrayList(new int[]{nodes.get(1), nodes.get(3)}))
				.addNonterminalEdge(ntRight, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();		
	}
	
	public static HeapConfiguration getExpected_stackCanonization_longStack(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AbstractStackSymbol abs = AbstractStackSymbol.get("X");
		
		ArrayList<StackSymbol> stack = new ArrayList<>();
		stack.add(abs);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false,true}, stack);
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1)}))
				.build();
	}
	
	public static HeapConfiguration getInput_stackCanonization_Blocked(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		ConcreteStackSymbol s = ConcreteStackSymbol.getStackSymbol("s", false);
		ConcreteStackSymbol bottom = ConcreteStackSymbol.getStackSymbol("Z", true);
		
		List<StackSymbol> stack = new ArrayList<>();
		stack.add(s);
		stack.add(s);
		stack.add(bottom);
		IndexedNonterminal nt = new IndexedNonterminalImpl("B", 2, new boolean[]{false, true}, stack);
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "");
		
		Type type = TypeFactory.getInstance().getType("AVLTree");
		Type nullType = TypeFactory.getInstance().getType("NULL");
		
		TIntArrayList nodes = new TIntArrayList();
		HeapConfigurationBuilder builder = hc.builder();
		return builder.addNodes(type, 3, nodes)
				.addNodes(nullType, 1, nodes)
				.addSelector(nodes.get(0), left, nodes.get(1))
				.addSelector(nodes.get(0), right, nodes.get(2))
				.addSelector(nodes.get(1), right, nodes.get(3))
				.addSelector(nodes.get(1), left, nodes.get(3))
				.addVariableEdge("null", nodes.get(3))
				.addNonterminalEdge(nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3)}))
				.build();			
	}
	
	public static HeapConfiguration getInput_practicalCanonize(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		
		Type nodeType = TypeFactory.getInstance().getType("AVLTree");
		Type nullType = TypeFactory.getInstance().getType("NULL");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(nodeType, 2, nodes)
					.addNodes(nullType, 1, nodes)
					.addSelector(nodes.get(0), parent, nodes.get(1))
					.addSelector(nodes.get(0), left, nodes.get(2))
					.addSelector(nodes.get(0), right, nodes.get(2))
					.addSelector(nodes.get(1), parent, nodes.get(2))
					.addSelector(nodes.get(1), right, nodes.get(2))
					.addSelector(nodes.get(1), left, nodes.get(2))
					.addVariableEdge("null", nodes.get(2) )
					.build();
	}
	
	public static HeapConfiguration getInput_practicalCanonize2(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel("balancing", "");
		
		Type nodeType = TypeFactory.getInstance().getType("AVLTree");
		Type nullType = TypeFactory.getInstance().getType("NULL");
		Type zType = TypeFactory.getInstance().getType("int_0");
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(nodeType, 2, nodes)
					.addNodes(nullType, 1, nodes)
					.addNodes(zType, 1, nodes)
					.addSelector(nodes.get(0), parent, nodes.get(1))
					.addSelector(nodes.get(0), left, nodes.get(2))
					.addSelector(nodes.get(0), right, nodes.get(2))
					.addSelector(nodes.get(0), balance, nodes.get(3))
					.addSelector(nodes.get(1), parent, nodes.get(2))
					.addSelector(nodes.get(1), right, nodes.get(2))
					.addSelector(nodes.get(1), left, nodes.get(2))
					.addSelector(nodes.get(1), balance, nodes.get(3))
					.addVariableEdge("null", nodes.get(2) )
					.addVariableEdge("0", nodes.get(3))
					.build();
	}
	
	public static HeapConfiguration getInput_practicalCanonize3(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		List<StackSymbol> stack = new ArrayList<>();
		stack.add( ConcreteStackSymbol.getStackSymbol( "Z", true ) );
		boolean [] reductionTentacles = new boolean[]{false, true, true, true, true};
		IndexedNonterminal nt = new IndexedNonterminalImpl( "BTestpC3", 5, reductionTentacles, stack );
		
		AnnotatedSelectorLabel leftZ = new AnnotatedSelectorLabel("left", "0");
		AnnotatedSelectorLabel rightZ = new AnnotatedSelectorLabel("right", "0");
		AnnotatedSelectorLabel leftP = new AnnotatedSelectorLabel("left", "1");
		AnnotatedSelectorLabel rightM = new AnnotatedSelectorLabel("right", "-1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel("balancing", "");
		
		Type nodeType = TypeFactory.getInstance().getType("AVLTree");
		Type nullType = TypeFactory.getInstance().getType("NULL");

		Type mType = TypeFactory.getInstance().getType("int_-1");
		Type zType = TypeFactory.getInstance().getType("int_0");
		Type pType = TypeFactory.getInstance().getType( "int_1" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( nodeType, 3, nodes )
				.addNodes( nullType, 1, nodes )
				.addNodes( mType, 1, nodes )
				.addNodes( zType, 1, nodes )
				.addNodes( pType, 1, nodes )
				.addVariableEdge( "null", nodes.get( 3 ) )
				.addVariableEdge( "-1", nodes.get( 4 ) )
				.addVariableEdge( "0", nodes.get( 5 ) )
				.addVariableEdge( "1", nodes.get( 6 ) )
				.addSelector( nodes.get( 0 ), leftZ, nodes.get( 1 ) )
				.addSelector( nodes.get( 0 ),rightZ, nodes.get( 1 ) )
				.addSelector( nodes.get( 0 ), parent, nodes.get( 3 ) )
				.addSelector( nodes.get( 0 ), balance, nodes.get( 6 ) )
				.addVariableEdge( "x", nodes.get( 0 ) )
				.addSelector( nodes.get( 1 ), leftP, nodes.get( 2 ) )
				.addSelector( nodes.get( 1 ), rightM, nodes.get( 3 ) )
				.addSelector( nodes.get( 1 ), parent, nodes.get( 0 ) )
				.addSelector( nodes.get( 1 ), balance, nodes.get( 4 ) )
				.addSelector( nodes.get( 2 ), parent, nodes.get( 1 ))
				.addNonterminalEdge( nt, new TIntArrayList(new int[]{nodes.get(2), nodes.get(3), nodes.get(4), nodes.get(5), nodes.get(6)}) )
				.build();			
	}
	
	public static HeapConfiguration getEmbedding_practicalCanonize3(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		List<StackSymbol> stack = new ArrayList<>();
		stack.add( ConcreteStackSymbol.getStackSymbol( "Z", true ) );
		boolean [] reductionTentacles = new boolean[]{false, true, true, true, true};
		IndexedNonterminal nt = new IndexedNonterminalImpl( "BTestpC3", 5, reductionTentacles, stack );
		
		AnnotatedSelectorLabel leftP = new AnnotatedSelectorLabel("left", "1");
		AnnotatedSelectorLabel rightM = new AnnotatedSelectorLabel("right", "-1");
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel("parent", "");
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel("balancing", "");
		
		Type nodeType = TypeFactory.getInstance().getType("AVLTree");
		Type nullType = TypeFactory.getInstance().getType("NULL");

		Type mType = TypeFactory.getInstance().getType("int_-1");
		Type zType = TypeFactory.getInstance().getType("int_0");
		Type pType = TypeFactory.getInstance().getType( "int_1" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( nodeType, 2, nodes )
				.addNodes( nullType, 1, nodes )
				.addNodes( mType, 1, nodes )
				.addNodes( zType, 1, nodes )
				.addNodes( pType, 1, nodes )
				.setExternal( nodes.get( 0 ) )
				.setExternal( nodes.get( 2 ) )
				.setExternal( nodes.get( 3 ) )
				.setExternal( nodes.get( 4 ) )
				.setExternal( nodes.get( 5 ) )
				.addVariableEdge( "null", nodes.get( 2 ) )
				.addVariableEdge( "-1", nodes.get( 3 ) )
				.addVariableEdge( "0", nodes.get( 4 ) )
				.addVariableEdge( "1", nodes.get( 5 ) )
				.addSelector( nodes.get( 0 ), leftP, nodes.get( 1 ) )
				.addSelector( nodes.get( 0 ), rightM, nodes.get( 2 ) )
				.addSelector( nodes.get( 0 ), balance, nodes.get( 3 ) )
				.addSelector( nodes.get( 1 ), parent, nodes.get( 0 ))
				.addNonterminalEdge( nt, new TIntArrayList(new int[]{nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(4), nodes.get(5)}) )
				.build();			
	}
	
	public static HeapConfiguration getInput_Cononize_withInstNecessary(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "0" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "0" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol abs = AbstractStackSymbol.get( "X" );
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		ArrayList<StackSymbol> leftLeftStack = new ArrayList<>();
		leftLeftStack.add( abs );
		IndexedNonterminal leftLeftNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , leftLeftStack );
		
		ArrayList<StackSymbol> leftRightStack = new ArrayList<>( );
		leftRightStack.add( abs );
		IndexedNonterminal leftRightNt = new IndexedNonterminalImpl( "BTestInst", leftRightStack );
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( s );
		rightStack.add( abs );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BTestInst", rightStack );
		
		ArrayList<StackSymbol> pStack = new ArrayList<>();
		pStack.add( s );
		pStack.add( s );
		pStack.add( abs );
		IndexedNonterminal pNt = new IndexedNonterminalImpl( "PTestInst", 5, reductionTentacles, pStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 5, nodes )
							.addVariableEdge( "-1", nodes.get( 0 ) )
							.addVariableEdge( "0", nodes.get( 1 ) )
							.addVariableEdge( "1", nodes.get( 2 ) )
							.addVariableEdge( "null", nodes.get( 3 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addSelector( nodes.get( 5 ), left, nodes.get( 7 ))
							.addSelector( nodes.get( 7 ), parent, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), right, nodes.get( 8 ) )
							.addSelector( nodes.get( 8 ), parent, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( pNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(4)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.addNonterminalEdge( leftLeftNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(7)}) )
							.addNonterminalEdge( leftRightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(8)}) )
							.addVariableEdge( "x", nodes.get( 7 ) ) //hinter abstraction from leaves
							.addVariableEdge( "y", nodes.get( 8 ) ) //and ensure there is only one step
							.build();
	}
	
	public static HeapConfiguration getExpected_Cononize_withInstNecessary(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "0" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "0" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol abs = AbstractStackSymbol.get( "X" );
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>( );
		leftStack.add( abs );
		IndexedNonterminal leftNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , leftStack );
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( abs );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , rightStack );
		
		
		ArrayList<StackSymbol> pStack = new ArrayList<>();
		pStack.add( s );
		pStack.add( abs );
		IndexedNonterminal pNt = new IndexedNonterminalImpl( "PTestInst", 5, reductionTentacles, pStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 3, nodes )
							.addVariableEdge( "-1", nodes.get( 0 ) )
							.addVariableEdge( "0", nodes.get( 1 ) )
							.addVariableEdge( "1", nodes.get( 2 ) )
							.addVariableEdge( "null", nodes.get( 3 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( pNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(4)}) )
							.addNonterminalEdge( leftNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(5)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.addVariableEdge( "x", nodes.get( 5 ) ) //hinter abstraction from leaves
							.addVariableEdge( "y", nodes.get( 6 ) ) //and ensure there is only one step
							.build();
	}
	
	public static HeapConfiguration getRule_Cononize_withInstNecessary(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "0" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "0" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol var = StackVariable.getGlobalInstance();
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( var );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , rightStack );
		
		
		ArrayList<StackSymbol> pStack = new ArrayList<>();
		pStack.add( s );
		pStack.add( var );
		IndexedNonterminal pNt = new IndexedNonterminalImpl( "PTestInst", 5, reductionTentacles, pStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 3, nodes )
							.setExternal( nodes.get( 0 ) )
							.setExternal( nodes.get( 1 ) )
							.setExternal( nodes.get( 2 ) )
							.setExternal( nodes.get( 3 ) )
							.setExternal( nodes.get( 5 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( pNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(4)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.build();
	}
	
	public static HeapConfiguration getInput_Embedding5(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "0" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "0" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol abs = AbstractStackSymbol.get( "X" );
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>( );
		leftStack.add( s );
		leftStack.add( abs );
		IndexedNonterminal leftNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , leftStack );
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( s );
		rightStack.add( abs );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BTestInst", 5, reductionTentacles , rightStack );
		
		
		ArrayList<StackSymbol> pStack = new ArrayList<>();
		pStack.add( s );
		pStack.add( s );
		pStack.add( abs );
		IndexedNonterminal pNt = new IndexedNonterminalImpl( "PTestInst", 5, reductionTentacles, pStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 3, nodes )
							.addVariableEdge( "-1", nodes.get( 0 ) )
							.addVariableEdge( "0", nodes.get( 1 ) )
							.addVariableEdge( "1", nodes.get( 2 ) )
							.addVariableEdge( "null", nodes.get( 3 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( pNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(4)}) )
							.addNonterminalEdge( leftNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(5)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.build();
	}
	
	public static HeapConfiguration getInput_AnnotationMaintaining(){
		HeapConfiguration  hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol abs = AbstractStackSymbol.get( "X" );
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>( );
		leftStack.add( s );
		leftStack.add( abs );
		IndexedNonterminal leftNt = new IndexedNonterminalImpl( "BT", 5, reductionTentacles , leftStack );
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( abs );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BT", 5, reductionTentacles , rightStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 3, nodes )
							.addVariableEdge( "-1", nodes.get( 0 ) )
							.addVariableEdge( "0", nodes.get( 1 ) )
							.addVariableEdge( "1", nodes.get( 2 ) )
							.addVariableEdge( "null", nodes.get( 3 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( leftNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(5)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.build();
	}
	
	public static HeapConfiguration getExpected_AnnotationMaintaining(){
		HeapConfiguration  hc = new InternalHeapConfiguration();
		
		AnnotatedSelectorLabel left = new AnnotatedSelectorLabel( "left", "1" );
		AnnotatedSelectorLabel right = new AnnotatedSelectorLabel( "right", "-1" );
		AnnotatedSelectorLabel parent = new AnnotatedSelectorLabel( "parent", "" );
		AnnotatedSelectorLabel balance = new AnnotatedSelectorLabel( "balance", "" );
		
		StackSymbol s = ConcreteStackSymbol.getStackSymbol( "s", false );
		StackSymbol abs = AbstractStackSymbol.get( "X" );
		boolean [] reductionTentacles = new boolean []{true,true,true,true,false};
		
		ArrayList<StackSymbol> leftStack = new ArrayList<>( );
		leftStack.add( s );
		leftStack.add( abs );
		IndexedNonterminal leftNt = new IndexedNonterminalImpl( "BT", 5, reductionTentacles , leftStack );
		
		ArrayList<StackSymbol> rightStack = new ArrayList<>();
		rightStack.add( abs );
		IndexedNonterminal rightNt = new IndexedNonterminalImpl( "BT", 5, reductionTentacles , rightStack );
		
		Type int_m = TypeFactory.getInstance().getType( "int_-1" );
		Type int_z = TypeFactory.getInstance().getType( "int_0" );
		Type int_p = TypeFactory.getInstance().getType( "int_1" );
		Type nullType = TypeFactory.getInstance().getType( "NULL" );
		Type nodeType = TypeFactory.getInstance().getType( "AVLTree" );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes( int_m, 1, nodes )
							.addNodes( int_z, 1, nodes )
							.addNodes( int_p, 1, nodes )
							.addNodes( nullType, 1, nodes )
							.addNodes( nodeType, 3, nodes )
							.addVariableEdge( "-1", nodes.get( 0 ) )
							.addVariableEdge( "0", nodes.get( 1 ) )
							.addVariableEdge( "1", nodes.get( 2 ) )
							.addVariableEdge( "null", nodes.get( 3 ) )
							.addSelector( nodes.get( 4 ), left, nodes.get( 5 ) )
							.addSelector( nodes.get( 5 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), right, nodes.get( 6 ) )
							.addSelector( nodes.get( 6 ), parent, nodes.get( 4 ) )
							.addSelector( nodes.get( 4 ), balance, nodes.get( 1 ) )
							.addNonterminalEdge( leftNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(5)}) )
							.addNonterminalEdge( rightNt, new TIntArrayList(new int[]{nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3), nodes.get(6)}) )
							.build();
	}
	

	
}

