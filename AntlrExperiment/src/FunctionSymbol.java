
public class FunctionSymbol extends Symbol {
	Scope currentScope;
	Symbol[] args;
	public FunctionSymbol(String name, String type, int functionLine, Scope currentScope){
		super(name, type, functionLine);
		this.currentScope = currentScope;
	}
	
	public Scope getCurrentScope(){
		return currentScope;
	}
}
