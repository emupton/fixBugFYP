package datastructures;

public enum StaticAnalysisTool {
	SCAN_BUILD("scan_build"),
	CPP_CHECK("cpp_check");
	private final String tool;
	StaticAnalysisTool(String tool){
		this.tool=tool;
	}
}
