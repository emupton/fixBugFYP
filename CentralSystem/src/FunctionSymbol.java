
public class FunctionSymbol extends Symbol {
	Scope currentScope;
	public FunctionSymbol(String name, String type, int lineNumber, Scope currentScope){
		super(name, type, lineNumber);
		this.currentScope = currentScope;
	}
	
	public Scope getCurrentScope(){
		return currentScope;
	}
}
