package datastructures;

public enum BugIdentifier {
	RESOURCE_LEAK("resourceLeak"),
	MALLOC_MISMATCH("Result of 'malloc' is converted to a pointer of type"),
	MEMORY_LEAK("Potential memory leak"),
	MEMORY_LEAK_2("Potential leak of memory"),
	DOUBLE_FREE("Attempt to free released memory");
	
	String cppCheckId;
	
	BugIdentifier(String cppCheckId ){
		this.cppCheckId = cppCheckId;
	}
	
	public String getCppCheckId(){
		return cppCheckId;
	}
	
	public static BugIdentifier getBugIdentifierByCppCheckId(String cppCheckId){
		for(BugIdentifier bug : BugIdentifier.values()){
			if(cppCheckId.contains(bug.getCppCheckId()) || cppCheckId.equals(bug.getCppCheckId())){
				return bug;
			}
		}
		return null;
	}
}
