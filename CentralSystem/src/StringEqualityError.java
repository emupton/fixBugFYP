
public class StringEqualityError {
	
	private String identifierA;
	private String identifierB;
	
	private int lineNumber;
	
	public StringEqualityError(String identifierA, String identifierB, int lineNumber){
		this.identifierA = identifierA;
		this.identifierB = identifierB;
		this.lineNumber = lineNumber;
	}
	
	public String getIdentifierA(){
		return identifierA;
	}
	
	public String getIdentifierB(){
		return identifierB;
	}
	
	public int getLineNumber(){
		return lineNumber;
	}
}
