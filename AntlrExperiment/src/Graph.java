import java.util.Set;

import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.antlr.v4.runtime.misc.MultiMap;

public class Graph {
	Set<String> nodes = new OrderedHashSet<String>();
	MultiMap<String, String> edges = new MultiMap<String, String>();
	
	public void edge(String source, String target){
		edges.map(source, target);
	}
	
	public String toDOT(){
		StringBuilder buf = new StringBuilder();
		buf.append("diagraph G{\n");
		buf.append(" ranksep=.25;\n");
		buf.append(" edge [arrowsize=.5]\n");
		buf.append(" node [shape=circle, fontname=\"ArialNarrow\",\n"); buf.append(" fontsize=12, fixedsize=true, height=.45];\n"); buf.append(" ");
		for (String node : nodes) { // print all nodes first
		        buf.append(node);
		buf.append("; "); }
		buf.append("\n");
		for (String src : edges.keySet()) {
		for (String trg : edges.get(src)) { buf.append(" "); buf.append(src);
		buf.append(" -> "); buf.append(trg); buf.append(";\n");
		} }
		buf.append("}\n");
		return buf.toString();
		}
	public static class FunctionListener extends CBaseListener {
		Graph graph = new Graph();
		String currentFunctionName = null;
		
		public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx){
			currentFunctionName = ctx.declarator().directDeclarator().getChild(0).getText();
			graph.nodes.add(currentFunctionName);
		}
		
		public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx){
			String funcName = ctx.declarator().directDeclarator().getChild(0).getText();
			graph.edge(currentFunctionName, funcName);
		}
	}
}

