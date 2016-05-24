import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FixPattern {
	private String xmlFilePath;
	private ArrayList<Object> componentsOfFix;
	
	private String rawIdentifier = "";
	private String rawSecondaryIdentifier = "";
	private String rawType = "";
	private String problemLine = "";
	private String rawPostExpression = "";
	private String rawPreExpression = "";
	
	public FixPattern(String xmlFilePath){
		this.xmlFilePath = xmlFilePath;
		parseXml();
	}
	
	public void parseXml(){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFilePath);
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
			NodeList nList = doc.getElementsByTagName("fix");
			Element fix = (Element) nList.item(0);
			String name = fix.getAttribute("name");
			
			String nameFirstOp ="";
			Element firstOp = null;
			
			for(int i=0; i<nList.getLength(); i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					firstOp = (Element) fix.getChildNodes().item(1);
					nameFirstOp = firstOp.getNodeName();
				}
			}
			
			ArrayList<Object> componentsOfFix = new ArrayList<Object>();
			
			switch(nameFirstOp){
			case "insert":
				componentsOfFix.add(FileOperation.INSERT);
				break;
			case "delete":
				componentsOfFix.add(FileOperation.DELETE);
				break;
			case "replace":
				componentsOfFix.add(FileOperation.REPLACE);
				break;
			}
			
			nList = firstOp.getChildNodes();
			
			for(int i=0; i<nList.getLength(); i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					Element eElement = (Element) nNode;
					String nodeName = eElement.getNodeName();
					if(nodeName.equals("instanceSpecificValue")){
						String isvLabel = eElement.getTextContent();
						isvLabel = isvLabel.replaceAll("\\s+", "");
						InstanceSpecificValue isv = InstanceSpecificValue.getEnumValueByLabel(isvLabel);
						componentsOfFix.add(isv);
					}
					
					if(nodeName.equals("rawstring")){
						componentsOfFix.add(eElement.getTextContent().replaceAll("\t", "").replaceAll("\"", "").replaceAll("\n", ""));
					}
					
				}
			}
			this.componentsOfFix = componentsOfFix;
		}
	}
	
	public void setIdentifier(String identifier){
		rawIdentifier = identifier;
	}
	
	public void setType(String type){
		rawType = type;
	}
	
	public void setSecondaryIdentifier(String secondaryIdentifier){
		this.rawSecondaryIdentifier = secondaryIdentifier;
	}
	
	public void setProblemLine(String line){
		problemLine = line;
	}
	
	public void setRawPostExpression(String poste){
		rawPostExpression = poste;
	}
	
	public void setRawPreExpression(String pree){
		rawPreExpression = pree;
	}
	
	public void applyFix(File problemFile, File outputFile, int lineNo){
		FileOperation fo = (FileOperation) componentsOfFix.get(0);
		String line = "";
		for(int i=1; i<componentsOfFix.size(); i++){
			Object obj = componentsOfFix.get(i);
			if(obj instanceof InstanceSpecificValue){
				if(obj.equals(InstanceSpecificValue.TYPE)){
					line+= rawType;
				}
				if(obj.equals(InstanceSpecificValue.IDENTIFIER)){
					line+= rawIdentifier;
				}
				if(obj.equals(InstanceSpecificValue.ENTIRE_LINE)){
					line+= problemLine;
				}
				if(obj.equals(InstanceSpecificValue.POST_EXPRESSION)){
					line+= rawPostExpression;
				}
				if(obj.equals(InstanceSpecificValue.PRE_EXPRESSION)){
					line+= rawPreExpression;
				}
				if(obj.equals(InstanceSpecificValue.SECONDARY_IDENTIFIER)){
					line+= rawSecondaryIdentifier;
				}
			}
			else{
				line+= obj.toString();
			}
		}
		switch (fo){
		case INSERT:
			try {
				FileHandlingUtils.insertLineAt(problemFile, outputFile, line, lineNo);
				System.out.println("Fix successfully applied to " + outputFile.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case REPLACE:
			try{
				FileHandlingUtils.writeLineAt(problemFile, outputFile, line, lineNo);
				System.out.println("Fix successfully applied to " + outputFile.getPath());
			} catch (IOException e){
				e.printStackTrace();
			}
			break;
		case DELETE:
			try {
				FileHandlingUtils.deleteLineAt(problemFile, outputFile, lineNo);
				System.out.println("Deletion. Fix successfully applied to " + outputFile.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			break;
		}
	}
	
}
