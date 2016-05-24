
public class Symbol {
	private String name = "";
	private String type = "";
	private int lineNumber;
	
	public Symbol(String name, String type, int lineNumber){
		this.name = name;
		this.type = type;
		this.lineNumber = lineNumber;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public int getLineNumber(){
		return lineNumber;
	}
	
}