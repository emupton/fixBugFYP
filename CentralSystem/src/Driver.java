import java.io.BufferedInputStream;

import datastructures.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class Driver {
	public static void main(String args[]) throws IOException{

		System.out.println("Loaded central system successfully");
		
		boolean customBugFinder = false;
		
		if(args[0].equals("0")){
			customBugFinder = true;
			System.out.println("Custom bug finder mode");
		}
		if(customBugFinder){
			String filePath = args[1];
			String outputPath = args[2];
			
			FileInputStream sourceFile = new FileInputStream(filePath);
			
			ANTLRInputStream inputStream = new ANTLRInputStream(sourceFile);
			CLexer lexer = new CLexer(inputStream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			CParser parser = new CParser(tokens);
			ParseTree tree = parser.compilationUnit();
			ParseTreeWalker walker = new ParseTreeWalker();
			StringEqualityListener listener = new StringEqualityListener();
			walker.walk(listener, tree);
			//first instance; initialising values in symbol table
			walker.walk(listener, tree);
			//second instance; checking for errors
			
			if(listener.isError()){
				StringEqualityError err = listener.getError();
				System.out.println("Error detected by central system at line " + err.getLineNumber());
				FixPattern fp = new FixPattern("../fixBugAntlr/FixPatterns/stringequality.xml");
				fp.setIdentifier(err.getIdentifierA());
				fp.setSecondaryIdentifier(err.getIdentifierB());
				String problemLine = FileHandlingUtils.getLineAt(new File(filePath), err.getLineNumber());
				String[] arrA = problemLine.split(err.getIdentifierA());
				String[] arrB = problemLine.split(err.getIdentifierB());
				fp.setRawPreExpression(arrA[0]);
				fp.setRawPostExpression(arrB[arrB.length-1]);
				fp.applyFix(new File(filePath), new File(outputPath), err.getLineNumber());
			}
			return;
		}
		String inputBugSerPath = args[0];
		Bug bug = null;
		try{
			InputStream file = new FileInputStream(inputBugSerPath);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try{
				bug = (Bug)input.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Failure to read in bug");
			}
			finally{
				input.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		BugIdentifier bugIdentifier = bug.getBugIdentifier();
		String bugMessage = bug.getBugMessage();
		StaticAnalysisTool tool = bug.getTool();
		
		File problemFile = new File(bug.getProblemFile());
		File outputFile = new File(args[1]);
		
		if(tool.equals(StaticAnalysisTool.SCAN_BUILD)){
			switch(bugIdentifier){
				case MALLOC_MISMATCH:
				{
					int lineNumber = bug.getLine();
					//in this instance, because of a comma in the bug descriptor, relevant line numbers begin at index 2 in the parsed specifics array
					String input = Files.readAllLines(Paths.get(bug.getProblemFile())).get(lineNumber-1);
					//
					ANTLRInputStream inputStream = new ANTLRInputStream(input);
					// create a lexer that feeds off of input 
					CLexer lexer = new CLexer(inputStream);
					// create a buffer of tokens pulled from the lexer 
					CommonTokenStream tokens = new CommonTokenStream(lexer);
					CParser parser = new CParser(tokens);
					
					ParseTree tree = parser.declaration();
					// Create a generic parse tree walker that can trigger callbacks
					ParseTreeWalker walker = new ParseTreeWalker();
					
					//getting the appropriate declarator type
					CListenerType listenerType = new CListenerType();
					walker.walk(listenerType, tree);
					String type = listenerType.getType();
					Token typeToken = listenerType.getTypeToken();
					tree = parser.initDeclarator();
					
					//updating the value of sizeof with the previously obtained type
					/*CListenerUpdate listenerUpdate = new CListenerUpdate(parser, type, typeToken);
					walker.walk(listenerUpdate, tree);
					TokenStream updatedStream = listenerUpdate.getUpdatedStream();
					String text = updatedStream.getText();
					text = listenerUpdate.getUpdatedLine();
					String partA = text.substring(0, type.length());
					String partB = text.substring(type.length());
					text = partA + " " + partB;*/
					//has trouble parsing declarationSpecifier along with rest of statement...
					
					FixPattern fp = new FixPattern("../FixPatterns/mallocmismatch.xml");
					fp.setType(type);
					fp.setIdentifier(listenerType.getIdentifier());
					fp.applyFix(problemFile, outputFile, lineNumber);
					
					//FileHandlingUtils.writeLineAt(problemFile, outputFile, text, lineNumber);
					break;
				}
				case MEMORY_LEAK_2:{
					int lineNumber = bug.getLine();
					//in this instance, because of a comma in the bug descriptor, relevant line numbers begin at index 2 in the parsed specifics array
					String input = Files.readAllLines(Paths.get(bug.getProblemFile())).get(lineNumber-1);
					//
					ANTLRInputStream inputStream = new ANTLRInputStream(input);
					// create a lexer that feeds off of input 
					CLexer lexer = new CLexer(inputStream);
					// create a buffer of tokens pulled from the lexer 
					CommonTokenStream tokens = new CommonTokenStream(lexer);
					CParser parser = new CParser(tokens);
					
					ParseTree tree = parser.declaration();
					// Create a generic parse tree walker that can trigger callbacks
					ParseTreeWalker walker = new ParseTreeWalker();
					
					//getting the appropriate declarator type
					CListenerType listenerType = new CListenerType();
					walker.walk(listenerType, tree);
					String type = listenerType.getType();
					Token typeToken = listenerType.getTypeToken();
					tree = parser.initDeclarator();
					
					//updating the value of sizeof with the previously obtained type
					/*CListenerUpdate listenerUpdate = new CListenerUpdate(parser, type, typeToken);
					walker.walk(listenerUpdate, tree);
					TokenStream updatedStream = listenerUpdate.getUpdatedStream();
					String text = updatedStream.getText();
					text = listenerUpdate.getUpdatedLine();
					String partA = text.substring(0, type.length());
					String partB = text.substring(type.length());
					text = partA + " " + partB;*/
					//has trouble parsing declarationSpecifier along with rest of statement...
					
					lineNumber = bug.getLines()[1];
					
					FixPattern fp = new FixPattern("../FixPatterns/memoryleak.xml");
					fp.setIdentifier(listenerType.getIdentifier());
					fp.applyFix(problemFile, outputFile, lineNumber);
					break;}
				case MEMORY_LEAK:{
					int[] lines = bug.getLines();
					int lineNumber = lines[0];
					//testing memory leak problem, need to get identifier p...
					String input = Files.readAllLines(Paths.get(bug.getProblemFile())).get(lineNumber-1);
					ANTLRInputStream inputStream = new ANTLRInputStream(input);
					// create a lexer that feeds off of input 
					CLexer lexer = new CLexer(inputStream);
					// create a buffer of tokens pulled from the lexer 
					CommonTokenStream tokens = new CommonTokenStream(lexer);
					CParser parser = new CParser(tokens);
					
					//ParseTree tree = parser.externalDeclaration();
					ParseTree tree = parser.expressionStatement();

					// Create a generic parse tree walker that can trigger callbacks
					ParseTreeWalker walker = new ParseTreeWalker();
					
					//getting the identifier
					CListenerAllocIdentifier listenerType = new CListenerAllocIdentifier();
					walker.walk(listenerType, tree);
					
					String identifier = listenerType.getIdentifier();
					
					//in future iterations determine whitespace by obtaining chars before non-whitespace char and prefixing to fixed line...
					lineNumber = lines[1];
					System.out.println(lineNumber);
					
					FixPattern fp = new FixPattern("../FixPatterns/memoryleak.xml");
					fp.setIdentifier(identifier);
					fp.applyFix(problemFile, outputFile, lineNumber);
					break;}
				case DOUBLE_FREE:{
					int[] lines = bug.getLines();
					
					int lineNumber = lines[2];
					
					System.out.println(lineNumber);
					
					FixPattern fp = new FixPattern("../FixPatterns/doublefree.xml");
					fp.applyFix(problemFile, outputFile, lineNumber);
					break;}
				default:
					//do nothing
					break;
		}
		}
		
		if(tool.equals(StaticAnalysisTool.CPP_CHECK)){
			System.out.println();
			switch(bugIdentifier){
			case RESOURCE_LEAK:
				FileInputStream sourceFile = new FileInputStream(bug.getProblemFile());
				
				ANTLRInputStream inputStream = new ANTLRInputStream(sourceFile);
				CLexer lexer = new CLexer(inputStream);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				CParser parser = new CParser(tokens);
				ParseTree tree = parser.compilationUnit();
				ParseTreeWalker walker = new ParseTreeWalker();
				
				ResourceByScope obtainIdentifier = new ResourceByScope();
				walker.walk(obtainIdentifier, tree);
				Symbol symbol = obtainIdentifier.getSymbolOfTypeByLineScope(bug.getLine(), "FILE");
				String identifier = symbol.getName();
				
				FixPattern fp = new FixPattern("../FixPatterns/fileresourceleak.xml");
				
				fp.setIdentifier(identifier);
				
				fp.applyFix(problemFile, outputFile, bug.getLine());}
				
		}
		
	}
}