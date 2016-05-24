
public enum FileOperation {
	INSERT("insert"),
	REPLACE("replace"),
	DELETE("delete");
	
	private String label;
	
	FileOperation (String label){
		this.label = label;
	}
}
