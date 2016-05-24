import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;


public class CListenerUpdate extends CBaseListener {
	private CParser parser;
	private TokenStream updatedStream;
	private String updatedLine;
	private String type;
	private Token typeToken;
	
	/**
	 * Used for updating the type of an expression
	 * 
	 * @param parser
	 * @param type
	 * @param typeToken
	 */
	public CListenerUpdate(CParser parser, String type, Token typeToken){
		super(parser);
		this.parser = parser;
		this.type = type;
		this.typeToken = typeToken;
	}
	
	public CListenerUpdate(){
		
	}
	
	public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) { 
	  	Token token = ctx.start;
	  	int index = token.getTokenIndex();
	  	TokenStream ts = parser.getInputStream();
	  	TokenStreamRewriter rewriter = new TokenStreamRewriter(ts);
	  	rewriter.replace(index, type);
	  	updatedLine = rewriter.getText();
	  	ts.index();
	  	updatedStream = rewriter.getTokenStream();
	}
	
	public TokenStream getUpdatedStream(){
		return updatedStream;
	}
	
	public String getUpdatedLine(){
		return updatedLine;
	}
}
