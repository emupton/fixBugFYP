
public class StringEqualityException extends Exception {
	
	String identifierA;
	String identifierB;
	
	int lineNumber;
	
	public StringEqualityException(String identifierA, String identifierB, int lineNumber){
		super();
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
	
}
