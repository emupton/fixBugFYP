import datastructures.BugIdentifier;


public enum InstanceSpecificValue {
	IDENTIFIER("IDENTIFIER"),
	SECONDARY_IDENTIFIER("SECONDARY_IDENTIFIER"),
	PRE_EXPRESSION("PRE_EXPRESSION"),
	POST_EXPRESSION("POST_EXPRESSION"),
	TYPE("TYPE"),
	ENTIRE_LINE("ENTIRE_LINE");
	
	private String label;
	
	InstanceSpecificValue(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public static InstanceSpecificValue getEnumValueByLabel(String label){
		for(InstanceSpecificValue isv : InstanceSpecificValue.values()){
			if(label.equals(isv.getLabel())){
				return isv;
			}
		}
		return null;
	}
}
