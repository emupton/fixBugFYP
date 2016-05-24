import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import datastructures.*;


public class CppCheckProcessor {
	String filePath;
	
	public CppCheckProcessor(String filePath){
		this.filePath = filePath;
	}
	
	public Bug[] process(){
	File xmlFile = new File(filePath);
	//creating a new XML file
	
	//general logic for parsing the XML file and extracting the bug objects
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	try {
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(doc!=null){
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("error");
		Bug[] bugs = new Bug[nList.getLength()];
		//getting error elements
		for(int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) nNode;
				String id = eElement.getAttribute("id");
				BugIdentifier bug = BugIdentifier.getBugIdentifierByCppCheckId(id);
				String message = eElement.getAttribute("msg");
				String file = eElement.getAttribute("file");
				int line = Integer.parseInt(eElement.getAttribute("line"));
				
				bugs[i] = new Bug(bug, StaticAnalysisTool.CPP_CHECK, file, message, line);
			}
		}
		return bugs;
	}
	return null;
	}
}
