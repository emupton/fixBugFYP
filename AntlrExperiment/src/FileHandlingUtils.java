import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * File handling utility class, contains various static methods used for
 * manipulating the input and output .c files
 * 
 * @author emma
 *
 */
public class FileHandlingUtils {
	/**
	 * Creates a duplicated version of 'input', however, at line 'lineNumber' 'input''s
	 * line is over-written with 'lineReplacement' in the 'output' file.
	 * 
	 * @param input
	 * @param output
	 * @param lineReplacement
	 * @param lineNumber
	 * @return
	 * @throws IOException
	 */
	
	public static String getLineAt(File file, int lineNumber) throws IOException{
		int lineNo = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String line="";
			while((line = br.readLine())!=null){
				if(lineNo == lineNumber){
					br.close();
					return line;
				}
				lineNo++;
			}
		}
		return "";
	}
	public static boolean writeLineAt(File input, File output, String lineReplacement, int lineNumber) throws IOException{
		FileWriter fw = new FileWriter(output);
		int lineNo = 0;
		System.out.println("Writing solution to: " + output.getAbsolutePath());
		try (BufferedReader br = new BufferedReader(new FileReader(input))){
			String line="";
			while((line = br.readLine())!=null){
				String newLine;
				if(lineNo == lineNumber-1){
					newLine = lineReplacement;
				}
				else{
					newLine = line;
				}
				fw.write(newLine +"\n");
				lineNo++;
			}
		}
		fw.close();
		return false;
	}
	
	/**
	 * * Creates a duplicated version of 'input', however, at line 'lineNumber' 'input''s
	 * line is over-written with 'lineReplacement' in the 'output' file.
	 * 
	 * Let lineNumber be N.
	 * 
	 * In the output file line N from input file becomes line N+1, with lineReplacement being the contents
	 * of line N in the output file.
	 * 
	 * @param input
	 * @param output
	 * @param lineInsert
	 * @param lineNumber
	 * @return
	 * @throws IOException
	 */
	public static boolean insertLineAt(File input, File output, String lineReplacement, int lineNumber) throws IOException{
		FileWriter fw = new FileWriter(output);
		int lineNo = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(input))){
			String line="";
			while((line = br.readLine())!=null){
				if(lineNo == lineNumber-1){
					fw.write(lineReplacement +"\n");
					fw.write(line + "\n");
				}
				else{
					fw.write(line + "\n");
				}
				lineNo++;
			}
		}
		fw.close();
		return false;
	}
	
	public static boolean deleteLineAt(File input, File output, int lineNumber) throws IOException {
		FileWriter fw = new FileWriter(output);
		int lineNo = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(input))){
			String line="";
			while((line = br.readLine())!=null){
				if(lineNo == lineNumber-2){
					//do nothing
				}
				else{
					fw.write(line + "\n");
				}
				lineNo++;
			}
		}
		fw.close();
		return false;
	}
}
