import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import datastructures.Bug;


public class Driver {
	public static void main(String args[]){
		String bugDescriptorXml = args[0];
		String outputBugSer = args[1];
		
		CppCheckProcessor processor = new CppCheckProcessor(bugDescriptorXml);
		Bug[] bugs = processor.process();
		
		Bug bug = bugs[0];
		try{
			OutputStream file = new FileOutputStream(outputBugSer);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try{
				output.writeObject(bug);
				System.out.println("Bug written successfully");
			}
			finally{
				output.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Failure to output bug");
		}
	}
}
