import java.util.ArrayList;


public class Scope {
	private Symbol symbol;
	private Scope enclosingScope;
	private int beginningLine;
	private int endLine;
	
	private ArrayList<Symbol> childSymbols = new ArrayList<Symbol>();
	public Scope(Symbol symbol){
		if(symbol==null){
			this.symbol = new Symbol("Global", "null", 0);
		}
		else{
			this.symbol = symbol;
		}
	}
	
	public void define(Symbol symbol){
		childSymbols.add(symbol);
	}
	
	public void setEnclosingScope(Scope enclosingScope){
		this.enclosingScope = enclosingScope;
	}
	
	public Scope getEnclosingScope(){
		return enclosingScope;
	}
	
	public String toString(){
		String contents="";
		System.out.println(symbol.getName() + ":");
		for(int i=0; i<childSymbols.size(); i++){
			contents += childSymbols.get(i).getType() + childSymbols.get(i).getName() + "\n";
		}
		return contents;
	}
	
	public void setBeginningLine(int line){
		beginningLine = line;
	}
	
	public void setEndLine(int line){
		endLine = line;
	}
	
	public int getEndLine(){
		return endLine;
	}
	
	public Symbol getSymbolByType(String type){
		for(int i=0; i<childSymbols.size(); i++){
			Symbol symbol = childSymbols.get(i);
			if(symbol.getType().equals(type)){
				return symbol;
			}
		}
		return null;
	}
	
	public Symbol getSymbolByIdentifier(String identifier){
		for(int i=0; i<childSymbols.size(); i++){
			Symbol symbol = childSymbols.get(i);
			if(symbol.getName().equals(identifier)){
				return symbol;
			}
		}
		return null;
	}
}
