import org.antlr.v4.runtime.Token;


/**
 * Listener to identify resource identifier of an 'alloc' statement
 * 
 * E.g malloc(p, sizeof(int))
 * 
 * We obtain p
 * 
 * @author emma
 *
 */
public class CListenerAllocIdentifier extends CBaseListener {
	
	String identifier = "";
	
	int hits = 0;
		
	public void enterPostfixExpression(CParser.PostfixExpressionContext ctx){
		hits++;
		if(hits==3){
			Token token = ctx.start;
			this.identifier = token.getText();
			System.out.println(identifier);
		}
	}
	
	public String getIdentifier(){
		return identifier;
	} 

}
