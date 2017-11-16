import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static org.w3c.dom.Node.*;

public class RssCategoryNews {
	private final String RSSCATNEWS = this.getClass().getSimpleName();
	
	public static final String CBS_RSS_URL = 
			"https://www.cbsnews.com/latest/rss/category";
	
	/* News Categories */
	public static final String CAT_WORLD = "world";
	public static final String CAT_SCITECH="tech";
	public static final String CAT_GAMECORE="scitech-gamecore";
	public static final String CAT_TOPSTORIES="main";
	
	private String url;
	private String cat;
	private String fileName;
	private ItemsListDB itemsDB;
	
	public static final String CBS_NEWS_RSS_FNAME = "xml/newsrss.xml";
	private final String SORRY_INFO="########Sorry, No details found, please check the link ######";
	private final String SORRY_LINK="########Sorry, No link found please go to https://www.cbsnews.com/ ######";
	
	public RssCategoryNews() {
		System.out.println(RSSCATNEWS+"():Object Creation");
		url = cat = fileName = null;
		itemsDB = ItemsListDB.getinstance();
	}//end RssCategoryNews()
	
	public void setCategory() {
		System.out.println(RSSCATNEWS+".setCategory():called");
		Scanner scan = new Scanner(System.in);
		System.out.println("News Type World(1), Tech(2), Games(3), Top Stories(4)");
		int type = scan.nextInt();
		scan.close();
		
		switch(type) {
			case 1:
			{
				this.cat = CAT_WORLD;
			}
			break;
			
			case 2:{
				this.cat = CAT_SCITECH;
			}
			break;
			
			case 3:{
				this.cat = CAT_GAMECORE;
			}
			break;
			
			case 4:{
				this.cat = CAT_TOPSTORIES;
			}
			break;
			
			default:{
				System.out.println("Wrong input, try again");
				System.exit(0);
			}
		}//end switch
	}//end setCategory()
	
	public void makeUrl() {
		System.out.println(RSSCATNEWS+".makeUrl():called");
		url = CBS_RSS_URL;
		fileName = CBS_NEWS_RSS_FNAME;
		System.out.println(RSSCATNEWS+".makeUrl():"+fileName);
		switch(cat){
			case CAT_WORLD:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_WORLD);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			case CAT_SCITECH:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_SCITECH);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);			
				}
			}
			break;
			
			case CAT_GAMECORE:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_GAMECORE);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			case CAT_TOPSTORIES:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_TOPSTORIES);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			default:
				break;
		}//end switch(cat)
	}//end makeUrl()
	
	public void getXML() {
		System.out.println(RSSCATNEWS+".getXML():called");
		
		HttpHandler sh = new HttpHandler();
		// Making a request to url and getting response
		try {
			sh.setFileName(fileName);
			sh.makeServiceCall(url);
			}catch(IOException ie){
				System.out.println(ie.getMessage());
				System.exit(0);
			}
	}//end getXML()
	
	private void extractAllItems(Node item) {
		System.out.println(RSSCATNEWS+".extractAllItems():called");
		
		NodeList iList = item.getChildNodes();
		int iLength = iList.getLength();
		System.out.println(RSSCATNEWS+".extractAllItems():iLength="+iLength);

		for(int i=0; i<iLength;i++) {
			String titleVal, linkVal, descVal;
			boolean instanceFound=false;
			Node cNode = iList.item(i);
			titleVal = linkVal = descVal = null;
			if (cNode == null) {
				//If node present continue to next item
				continue;
			}
			if (cNode instanceof Element) {
				//String content = cNode.getLastChild().getTextContent().trim();
			
				switch(cNode.getNodeName()) {		
				case "title":{
					System.out.println(RSSCATNEWS+".extractAllItems():title found");
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
					System.out.println(RSSCATNEWS+".extractAllItems():link found");
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
					System.out.println(RSSCATNEWS+".extractAllItems():description found");
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
		System.out.println(RSSCATNEWS+".extractChannelChildItems():called");
		
		NodeList nList = node.getChildNodes();
		int nLength = nList.getLength();
		System.out.println(RSSCATNEWS+".extractChannelChildItems():nLength="+nLength);
		for(int i=0; i<nLength;i++) {
			Node item = nList.item(i);
			if ((item instanceof Element) && item.getNodeName().equals("item")) {
				extractAllItems(item);
			}//end if
		}// end for
	}//end of extractChannelChildItems()
	
	public void domParser() {
		System.out.println(RSSCATNEWS+".domParser():called");
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
		try {
			//Get DOM builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Load and parser XML, document contains the entire XML as a tree
			System.out.println(RSSCATNEWS+".domParser():set filename="+fileName);
			
			Document document = builder.parse(new File(fileName));
			//Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();
			
			//Here comes the root node"
			Element root = (Element) document.getDocumentElement();
			NodeList nList = root.getChildNodes();
			for(int i=0; i<nList.getLength(); i++) {
				Node n = nList.item(i);
				if (n.getNodeType() == ELEMENT_NODE) {
					System.out.println(RSSCATNEWS+".domParser():Element["+i+"]="+n.getNodeName());
					if (n.getNodeName().equals("channel")) {
						extractChannelChildItems(n);
					}//endif
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
	}//end domParser()
	
	public void displayItems() {
		System.out.println(RSSCATNEWS+".displayItems():called");
		System.out.println("**************************************CBS NEWS*******************************");
		itemsDB.display();
	}//end displayItems()
}
