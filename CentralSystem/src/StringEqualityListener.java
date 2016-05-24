import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;


public class StringEqualityListener extends CBaseListener {
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
	private ArrayList<Scope> scopesArray = new ArrayList<Scope>();
	private Scope globals;
	private Scope currentScope;
	
	private boolean checked = false;
	
	private boolean isError = false;
	
	private StringEqualityError error = null;
	
	public void enterCompilationUnit(CParser.CompilationUnitContext ctx){
		globals = new Scope(null);
		currentScope = globals;
		currentScope.setBeginningLine(1);
	}
	public void exitCompilationUnit(CParser.CompilationUnitContext ctx){
		int endLine = ctx.getStop().getLine();
		currentScope.setEndLine(endLine);
		scopesArray.add(globals);
		checked = true;
	}
	
	public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx){
		Token functionTypeTok = ctx.declarationSpecifiers().getStop();
		int functionLine = functionTypeTok.getLine();
		int beginningLine = functionTypeTok.getLine() +1;
		String functionType = functionTypeTok.getText();
		String currentFunctionName = ctx.declarator().directDeclarator().getChild(0).getText();
		FunctionSymbol function = new FunctionSymbol(currentFunctionName, functionType, functionLine, currentScope);
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
		int lineNumber = declarationType.getLine();
//		String arrayChar = ctx.initDeclaratorList().initDeclarator().declarator().getChild(1).getText();
	//	if(arrayChar.equals("[")){
	//		System.out.println("Something...");
	//	}
		String type = declarationType.getText();
		String directDeclarator = ctx.initDeclaratorList().initDeclarator().declarator().directDeclarator().getText();
		boolean isString = directDeclarator.contains("[");
		//boolean isString = type.contains("*"); ???
		VariableSymbol var;
		if(!isString){
			var = new VariableSymbol(directDeclarator.toString(), type, lineNumber);
		}
		else{
			String identifier = directDeclarator.split("\\[")[0];
			var = new VariableSymbol(identifier, "string", lineNumber);
		}
		currentScope.define(var);
	}
	
	public void enterEqualityExpression(CParser.EqualityExpressionContext ctx) {
		if(checked && !isError) {
			String identifierA = ctx.start.getText();
			String identifierB = ctx.stop.getText();
			
			int lineNumber = ctx.start.getLine();
			
			if(currentScope.getSymbolByIdentifier(identifierA)!=null && currentScope.getSymbolByIdentifier(identifierB)!=null){
				boolean identifierAString = currentScope.getSymbolByIdentifier(identifierA).getType().equals("string");
				boolean identifierBString = currentScope.getSymbolByIdentifier(identifierB).getType().equals("string");
				
				if(identifierAString && identifierBString){
					isError = true;
					error = new StringEqualityError(identifierA, identifierB, lineNumber);
				}
			}
		}
	}
	
	public boolean isError(){
		return isError; 
	}
	
	public StringEqualityError getError(){
		return error;
	}
	
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
