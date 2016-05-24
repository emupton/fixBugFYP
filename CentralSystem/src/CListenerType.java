import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;


/**
 * Listener used when trying to obtain the type of a variable being declared by analysing
 * the LHS of a statement/expression
 * @author emma
 *
 */

public class CListenerType extends CBaseListener {
	private String type;
	private String identifier;
	private Token typeToken;
	private boolean checked = false;

	
	/* Obtains the first token from the expression and gets the token
	 * 
	 * (non-Javadoc)
	 * @see CBaseListener#enterTypeSpecifier(CParser.TypeSpecifierContext)
	 */
	public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) { 
		if(checked == false){
	  	Token token = ctx.start;
	  	typeToken = token;
	  	token.getTokenIndex();
	  	type = token.getText();
	  	ctx.getParent().getParent().toStringTree();
	  	checked=true;
		}
	}
	
	/**
	 * Obtains the first token from the primary expression, which will be the identifier of the variable
	 * 
	 * @param ctx
	 */
	public void enterDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
		Token token = ctx.start;
		identifier = token.getText();
	}

	public String getIdentifier(){
		return identifier;
	}
	
	public String getType(){
		return type;
	}
	
	public Token getTypeToken(){
		return typeToken;
	}
}
