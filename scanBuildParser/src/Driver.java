

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import datastructures.*;
public class Driver {
	public static void main(String args[]) throws IOException{
		String filename = args[0];
		String problemFile = args[1];
		String outputBugFile = args[2];
		
		File input = new File(filename);
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com");
		
		Elements matches = doc.getElementsByClass("simpletable");
		Element table = matches.first();
		
		Elements columns = table.getElementsByTag("td");
		Element bugDescriptor = columns.last();
		//getting the bug descriptor, which is the last column
		
		String description = bugDescriptor.ownText();
		//obtaining the bug description from the bug descriptor column
		
		matches = doc.getElementsByClass("code");
		table = matches.first();
		//getting the table that contains the problem code line numbers
		
		Elements rows = table.getElementsByTag("tr");
		//getting the rows
		Elements divs = table.getElementsByAttributeValueContaining("id","Path");
		//getting any elements that have "Path" in their id attribute; this means the previous row in the table has a bug
		//elements that have "Path" in their id attribute are describing the bug found in the preceding line
		ArrayList<Integer> lines = new ArrayList<Integer>();
		for(Element div : divs){
			//looping through div elements
			Node parent = div.parentNode().parentNode();
			Node lineColumn = parent.previousSibling().previousSibling();
			//logic to find the bug line
			Elements contents = ((Element) lineColumn).getElementsByAttributeValue("class", "num");
			lines.add(Integer.parseInt(contents.text()));
			//printing out the line number of the problem line
		}
		
		int[] ints = new int[lines.size()];
	    int i = 0;
	    for (Integer n : lines) {
	        ints[i++] = n;
	    }
	    
		Bug bug = new Bug(BugIdentifier.getBugIdentifierByCppCheckId(description), StaticAnalysisTool.SCAN_BUILD, problemFile, description, ints );
		
		try{
			OutputStream file = new FileOutputStream(outputBugFile);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try{
				output.writeObject(bug);
				System.out.println(outputBugFile + " successfully written to");
			}
			finally{
				output.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
