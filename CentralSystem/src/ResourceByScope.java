import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;


public class ResourceByScope extends CBaseListener {
	ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
	private ArrayList<Scope> scopesArray = new ArrayList<Scope>();
	private Scope globals;
	private Scope currentScope;
	
	public void enterCompilationUnit(CParser.CompilationUnitContext ctx){
		globals = new Scope(null);
		currentScope = globals;
		currentScope.setBeginningLine(1);
	}
	public void exitCompilationUnit(CParser.CompilationUnitContext ctx){
		int endLine = ctx.getStop().getLine();
		currentScope.setEndLine(endLine);
		scopesArray.add(globals);
	}
	
	public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx){
		Token functionTypeTok = ctx.declarationSpecifiers().getStop();
		int line = functionTypeTok.getLine();
		int beginningLine = functionTypeTok.getLine() +1;
		String functionType = functionTypeTok.getText();
		String currentFunctionName = ctx.declarator().directDeclarator().getChild(0).getText();
		FunctionSymbol function = new FunctionSymbol(currentFunctionName, functionType, line, currentScope);
		Scope parentScope = currentScope;
		currentScope.define(function);
		scopes.put(ctx, parentScope);
		Scope childScope = new Scope(function);
		childScope.setEnclosingScope(parentScope);
		currentScope = childScope; //push
		currentScope.setBeginningLine(beginningLine);
	}
	
	public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx){
		Token functionTypeTok = ctx.stop;
		int endLine = functionTypeTok.getLine();
		currentScope.setEndLine(endLine);
		scopesArray.add(currentScope);
		currentScope = currentScope.getEnclosingScope(); //pop
	}
	
	public void enterDeclaration(CParser.DeclarationContext ctx){
			Token declarationType = ctx.declarationSpecifiers().getStop();
			int line = declarationType.getLine();
			String type = declarationType.getText();
			String directDeclarator = ctx.initDeclaratorList().initDeclarator().declarator().directDeclarator().getText();
			VariableSymbol var = new VariableSymbol(directDeclarator.toString(), type, line);
			currentScope.define(var);
	}
	
	/**
	 * Gets the symbol in the scope of specified type. Scope determined
	 * by line which is the end line of the associated scope.
	 * 
	 * @param line
	 * @param type
	 * @return
	 */
	public Symbol getSymbolOfTypeByLineScope(int line, String type){
		for(int i=0; i<scopesArray.size(); i++){
			Scope scope = scopesArray.get(i);
			if(scope.getEndLine() == line){
				Symbol symbol = scope.getSymbolByType(type);
				return symbol;
			}
		}
		return null;
	}
}
