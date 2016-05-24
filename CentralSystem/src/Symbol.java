
public class Symbol {
	private String name;
	private String type;
	private int lineNumber;
	
	public Symbol(String name, String type, int lineNumber){
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
}