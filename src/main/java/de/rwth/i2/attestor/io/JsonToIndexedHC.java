package de.rwth.i2.attestor.io;

import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.HeapConfigurationBuilder;
import de.rwth.i2.attestor.indexedGrammars.AnnotatedSelectorLabel;
import de.rwth.i2.attestor.indexedGrammars.IndexedNonterminal;
import de.rwth.i2.attestor.indexedGrammars.stack.AbstractStackSymbol;
import de.rwth.i2.attestor.indexedGrammars.stack.ConcreteStackSymbol;
import de.rwth.i2.attestor.indexedGrammars.stack.StackSymbol;
import de.rwth.i2.attestor.indexedGrammars.stack.StackVariable;
import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.TypeFactory;
import gnu.trove.list.array.TIntArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * for the stack symbols we assume the following: 
 * StackVariable: "()"
 * AbstractStackSymbol: "_LABEL" (starts with underscore)
 * BottomSymbol: "Z" (starts with upper case letter)
 * normal Symbol: "s" (starts with lower case)
 * 
 * @author Hannah
 *
 */
public class JsonToIndexedHC {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger( "JsonToIndexedHC" );

	public static HeapConfiguration jsonToHC( JSONObject obj ) {
		
		HeapConfigurationBuilder builder = Settings.getInstance().factory().createEmptyHeapConfiguration().builder();

		JSONArray jsonNodes = obj.getJSONArray( "nodes" );
		TIntArrayList nodes = JsonToIndexedHC.parseNodes( builder, jsonNodes );
	
		JSONArray externals = obj.getJSONArray( "externals" );
		JsonToIndexedHC.parseExternals( builder, nodes, externals );
	
		JSONArray variables = obj.getJSONArray( "variables" );
		JsonToIndexedHC.parseVariables( builder, nodes, variables );
	
		JSONArray selectors = obj.getJSONArray( "selectors" );
		JsonToIndexedHC.parseSelectors( builder, nodes, selectors );
	
		JSONArray hyperedges = obj.getJSONArray( "hyperedges" );
		JsonToIndexedHC.parseHyperedges( builder, nodes, hyperedges );
	
		return builder.build();
	}

	private static void parseHyperedges( HeapConfigurationBuilder builder,
			TIntArrayList nodes, JSONArray hyperedges ) {
		
		for( int i = 0; i < hyperedges.length(); i++ ){
			JSONObject hyperedge = hyperedges.getJSONObject( i );
			String label = hyperedge.getString( "label" );
			
			List<StackSymbol> stack = parseStack( hyperedge.getJSONArray("stack") );
			
			IndexedNonterminal nt = (IndexedNonterminal) Settings.getInstance().factory().getNonterminal(label);
			nt = nt.getWithStack(stack);

			TIntArrayList tentacles = new TIntArrayList(nt.getRank());
			for( int tentacleNr = 0; tentacleNr < hyperedge.getJSONArray( "tentacles" ).length(); tentacleNr++){
				tentacles.add( nodes.get( hyperedge.getJSONArray( "tentacles" ).getInt( tentacleNr ) ) );
			}
			
			builder.addNonterminalEdge(nt, tentacles);
		}
	}
	
	static List<StackSymbol>  parseStack(JSONArray stack){
		List<StackSymbol> res = new ArrayList<>();
		for( int i = 0; i < stack.length(); i++ ){
			String symbol = stack.getString(i);
			if( symbol.equals("()") ){
				res.add( StackVariable.getGlobalInstance() );
				assert( i == stack.length() -1 ) : "variables should be the last symbol of a stack"; 
			}else if( symbol.startsWith("_") ){
				res.add( AbstractStackSymbol.get(symbol.substring(1)) );
				assert( i == stack.length() -1 ) : "abstract stack symbols may only occur at the end of stack";
			}else if( Character.isLowerCase(symbol.codePointAt(0)) ){
				res.add( ConcreteStackSymbol.getStackSymbol(symbol, false) );
				assert( i < stack.length() -1 ) : "stacks cannot end with a concrete non-bottom symbol";
			}else if( Character.isUpperCase( symbol.codePointAt(0)) ){
				res.add( ConcreteStackSymbol.getStackSymbol(symbol, true) );
				assert( i == stack.length() -1 ) : "bottom symbols have to be the last element of a stack";
			}
		}
		return res;
	}

	private static void parseSelectors( HeapConfigurationBuilder builder,
			TIntArrayList nodes, JSONArray selectors ) {
		for( int i = 0; i < selectors.length(); i++ ){
			String name = selectors.getJSONObject( i ).getString( "label" );
			String annotation = selectors.getJSONObject(i).getString("annotation");
			int originID = selectors.getJSONObject( i ).getInt( "origin" );
			int targetID = selectors.getJSONObject( i ).getInt( "target" );
			builder.addSelector( nodes.get( originID ), 
					new AnnotatedSelectorLabel(name, annotation),
					nodes.get( targetID ) );
		}
	}

	private static void parseVariables( HeapConfigurationBuilder builder,
			TIntArrayList nodes, JSONArray variables ) {
		for( int i = 0; i < variables.length(); i++ ){
			String name = variables.getJSONObject( i ).getString( "name" );
			int targetId = variables.getJSONObject( i ).getInt( "target" );
			builder.addVariableEdge( name, nodes.get( targetId ) );
		}
	}

	private static void parseExternals( HeapConfigurationBuilder builder,
			TIntArrayList nodes, JSONArray externals ) {
		
		for( int i = 0; i < externals.length(); i++ ){
			int nodeId = externals.getInt( i );
			builder.setExternal( nodes.get( nodeId ) );
		}
	}

	private static TIntArrayList parseNodes(HeapConfigurationBuilder builder, JSONArray jsonNodes ) {
		
		TIntArrayList nodes = new TIntArrayList();
		for( int i = 0; i < jsonNodes.length(); i++ ){
			String typeName = jsonNodes.getJSONObject( i ).getString( "type" );
			Type type = TypeFactory.getInstance().getType(typeName);
			int number = jsonNodes.getJSONObject( i ).getInt( "number" );
			builder.addNodes( type, number, nodes );
		}
		return nodes;
	}


}
