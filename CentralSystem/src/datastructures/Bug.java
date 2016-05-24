package datastructures;
import java.io.Serializable;

public class Bug implements Serializable{
	private BugIdentifier id;
	private StaticAnalysisTool tool;
	private String problemFile;
	private String bugMessage;
	private int line = -1;
	private int lines[] = null;
	
	public Bug(BugIdentifier id, StaticAnalysisTool tool, String problemFile, String bugMessage, int line){
		this.id = id;
		this.tool = tool;
		this.problemFile = problemFile;
		this.bugMessage = bugMessage;
		this.line = line;
	}
	
	public Bug(BugIdentifier id, StaticAnalysisTool tool, String problemFile, String bugMessage, int[] lines){
		this.id = id;
		this.tool = tool;
		this.problemFile = problemFile;
		this.bugMessage = bugMessage;
		this.lines = lines;
	}
	
	public int[] getLines(){
		if(lines!=null){
			return lines;
		}
		else{
			return new int[]{line};
		}
	}
	
	public int getLine(){
		if(line!=-1){
			return line;
		}
		else{
			return lines[0];
		}
	}
	
	public BugIdentifier getBugIdentifier(){
		return id;
	}
	
	public StaticAnalysisTool getTool(){
		return tool;
	}
	
	public String getProblemFile(){
		return problemFile;
	}
	
	public String getBugMessage(){
		return bugMessage;
	}
	
}
