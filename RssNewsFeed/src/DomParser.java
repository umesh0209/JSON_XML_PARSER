import static org.w3c.dom.Node.ELEMENT_NODE;

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

public class DomParser {
	
	private final String DOMPARSER = this.getClass().getSimpleName();
	private final String SORRY_INFO="########Sorry, No details found, please check the link ######";
	private final String SORRY_LINK="########Sorry, No link found please go to https://www.cbsnews.com/ ######";
	private ItemsListDB itemsDB;
	
	public DomParser() {
		// TODO Auto-generated constructor stub
		itemsDB = ItemsListDB.getinstance();
		
	}
	
	public void parseXML(String fileName) {
		System.out.println(DOMPARSER+".parseXML():called");
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
		try {
			//Get DOM builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Load and parser XML, document contains the entire XML as a tree
			System.out.println(DOMPARSER+".parseXML():set filename="+fileName);
			
			Document document = builder.parse(new File(fileName));
			//Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();
			
			//Here comes the root node"
			Element root = (Element) document.getDocumentElement();
			NodeList nList = root.getChildNodes();
			for(int i=0; i<nList.getLength(); i++) {
				Node n = nList.item(i);
				if (n.getNodeType() == ELEMENT_NODE) {
					System.out.println(DOMPARSER+".parseXML():Element["+i+"]="+n.getNodeName());
					if (n.getNodeName().equals("channel")) {
						extractChannelChildItems(n);
					}//end if
				}//end if
		    }//end for			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			System.exit(0);
		}catch ( IOException ie) {
			ie.printStackTrace();
			System.exit(0);
		}catch( SAXException saxe) {
			saxe.printStackTrace();
			System.exit(0);
		}
	}//end parseXML()
	
	
	/*************************************************************************************
	 *                                 PRIVATE FUNCTIONS                                 *
	 ************************************************************************************/                              
	private void extractAllItems(Node item) {
		System.out.println(DOMPARSER+".extractAllItems():called");
		
		NodeList iList = item.getChildNodes();
		int iLength = iList.getLength();
		System.out.println(DOMPARSER+".extractAllItems():iLength="+iLength);

		for(int i=0; i<iLength;i++) {
			String titleVal, linkVal, descVal;
			boolean instanceFound=false;
			Node cNode = iList.item(i);
			titleVal = linkVal = descVal = null;
			if (cNode == null) {
				//If no node present continue to next item
				continue;
			}
			if (cNode instanceof Element) {
				//String content = cNode.getLastChild().getTextContent().trim();
			
				switch(cNode.getNodeName()) {		
				case "title":{
					System.out.println(DOMPARSER+".extractAllItems():title found");
					instanceFound = true;
					Node lastChild = cNode.getLastChild();
					//check if last child is present(text)
					if (lastChild == null) {
						titleVal = SORRY_INFO;
					}
					else {
						titleVal = lastChild.getTextContent().trim();
					}
				}
				break;
				
				case "link":{
					System.out.println(DOMPARSER+".extractAllItems():link found");
					instanceFound = true;
					Node lastChild = cNode.getLastChild();
					if (lastChild == null) {
						linkVal = SORRY_LINK;
					}
					else {
						linkVal = lastChild.getTextContent().trim();
					}
				}
				break;
				
				case "description":{
					System.out.println(DOMPARSER+".extractAllItems():description found");
					instanceFound = true;
					Node lastChild = cNode.getLastChild();
					if (lastChild == null) {
						descVal = SORRY_INFO;
					}
					else {
						descVal = lastChild.getTextContent().trim();
					}
				}
				break;
				
				default: 
					break;
				}
				
				if ( instanceFound ) {
					//if element found then add it to the list
					NewsItem newsch = itemsDB.getNewsItem();
					newsch.setTitle(titleVal);
					newsch.setLink(linkVal);
					newsch.setDescription(descVal);
					itemsDB.addToList(newsch);
					instanceFound = false;
				}			
			}//end switch
		}// end of if(cNode instanceof Element)
	}//extractAllItems()
	
	private void extractChannelChildItems(Node node) {
		System.out.println(DOMPARSER+".extractChannelChildItems():called");
		
		NodeList nList = node.getChildNodes();
		int nLength = nList.getLength();
		System.out.println(DOMPARSER+".extractChannelChildItems():nLength="+nLength);
		for(int i=0; i<nLength;i++) {
			Node item = nList.item(i);
			if ((item instanceof Element) && item.getNodeName().equals("item")) {
				extractAllItems(item);
			}//end if
		}// end for
	}//end of extractChannelChildItems()
}
