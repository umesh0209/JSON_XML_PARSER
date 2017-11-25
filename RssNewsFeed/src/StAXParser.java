//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StAXParser extends XmlFunctions{

	private final String STAXPARSER = this.getClass().getSimpleName();
	private ItemsListDB cList ;
	//private String fileName;
	private int type;
	private SharedData sharedDataObj;
	private String url;
	Stack<String> startTagStack;
	private String content;
	private NewsItem newsItem;
	
	public StAXParser(String url) {
		System.out.println(STAXPARSER+"():called");
		sharedDataObj = SharedData.getInstance();
		type = sharedDataObj.getType();
		cList = sharedDataObj.getCatList().get(type).getItemsDB();
		//fileName = sharedDataObj.getCatList().get(type).getfName();
		startTagStack = new Stack<String>();
		this.url = url;
	}
	
	protected String getParsingClassString() {
		System.out.println(STAXPARSER+".matchParsingClassString():called");
		return STAXPARSER;
	}
	
	protected void parsingXML() {
		System.out.println(STAXPARSER+".parsingXML():called");
		
		URL ser_url=null;
		try {
			ser_url = new URL(url);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			InputStream is = ser_url.openStream();
			//InputStream is = new FileInputStream("xml/temp.xml");
			XMLStreamReader readEvent = factory.createXMLStreamReader(is);
			
			while(readEvent.hasNext()) {
				int event;
				
				readEvent.next();
				event= readEvent.getEventType();
				
				if ( event == XMLStreamConstants.START_ELEMENT) {
					staxStartElement(readEvent);
				}else if ( event == XMLStreamConstants.CHARACTERS) {
					staxCharacters(readEvent);
				}
				else if (event == XMLStreamConstants.END_ELEMENT) {
					staxEndElement(readEvent);
				}
			}//while(readEvent.hasNext())
		}catch(MalformedURLException me) {
			System.out.println(STAXPARSER+".parsingXML():Exception");
			me.printStackTrace();
		}catch(IOException ie) {
			System.out.println(STAXPARSER+".parsingXML():Exception");
			ie.printStackTrace();
		}catch( XMLStreamException xse) {
			System.out.println(STAXPARSER+".parsingXML():Exception");
			xse.printStackTrace();
		}
	}//parsingXML()
	
	
	/************************************************************************************************
	 *                                      PRIVATE FUNCTIONS                                       *
	 ************************************************************************************************/
	
	private void staxStartElement(XMLStreamReader rEvent) {
		System.out.println(STAXPARSER+".staxStartElement():called");
		
		String qName = rEvent.getName().toString();
		System.out.println(STAXPARSER+".staxStartElement(): qName="+ qName +" found");
		 
		if (qName.equals("channel")){
			System.out.println(STAXPARSER+".staxStartElement(): <channel> found, items will follow...");
			cList.createList();
			return;
		}else if (qName.equals("item")) {
			System.out.println(STAXPARSER+".staxStartElement(): <item> found, create an node");
			newsItem = cList.getNewsItem();
			startTagStack.push("item");
			System.out.println("Stack=>push:item");
			return;
		}
		
		//Before proceeding further check the stack, the length of the stack should be always max 2,
		// top=item related elements like title, desc, link, and bottom should be always <item> tag
		//for any other element ignore
		if (startTagStack.isEmpty()) {
			System.out.println(STAXPARSER+".staxStartElement(): Element not related to <item>");
			return;
		}
		
		String str;
		if (qName.equals("title")) {
			System.out.println(STAXPARSER+".staxStartElement(): <title> found");
			str = "title";
		}else if (qName.equals("description")) {
			System.out.println(STAXPARSER+".staxStartElement(): <description> found");
			str = "description";
		}else if (qName.equals("link")) {
			System.out.println(STAXPARSER+".staxStartElement(): <link> found");
			str = "link";
		}else {
			System.out.println(STAXPARSER+".staxStartElement(): <item> child element not ineteresed(ENI)");
			//Store it as Element Not interested (ENI) which are <image> <pubDate> <guid> of <item>
			str = "ENI";
		}
		
		//push the qName, as it is related to <item>, and need to be processed, if ENI contents
		//are not stored
		startTagStack.push(str);
		System.out.println("Stack=>push:"+str);
		content = null;
	}//end staxStartElement()
	
	private void staxCharacters(XMLStreamReader rEvent) {
    	int start = rEvent.getTextStart();
    	int length = rEvent.getTextLength();
	    String buffer = new String(rEvent.getTextCharacters(),start,length);
	    
	    System.out.println(STAXPARSER+".staxCharacters() called:"+buffer);
	    
	    //if stack is empty then the content is related to <desc,title,link>, of <item> so ignore
	    if(startTagStack.isEmpty()) {
	    	System.out.println(STAXPARSER+".staxCharacters():IGNORE THE CHARACTERS..."+buffer);
	    	return;
	    }
	    
	    if (startTagStack.peek().equals("ENI") == false) {
		    if(content == null ) {
		    	content = buffer;
		    }else {
		    	content = content.concat(buffer);
		    }
	    }
	}//staxCharacters(XMLStreamReader rEvent)
	
	private void staxEndElement(XMLStreamReader rEvent) {
		System.out.println(STAXPARSER+".staxEndElement():called");
		
		String stTag=null;
		//String qName = rEvent.getLocalName();
		String qName = rEvent.getName().toString();
		
		//This will happen when no <item> tag is added to stack
		if ( startTagStack.isEmpty() ) {
			System.out.println(STAXPARSER+".staxEndElement(): element not related to <item>, so ignore");
			return;
		}
		stTag = startTagStack.pop();
		System.out.println("Stack=>pop:"+stTag);

		// <title> </title>=>found
		if ( stTag.equals("title") && qName.equals("title")) {
				System.out.println(STAXPARSER+".staxEndElement():</title> found: adding title");
				newsItem.setTitle(content);
				content = null;
		}else if ( stTag.equals("description") && qName.equals("description")) {
			// <description> </description>=>found
			System.out.println(STAXPARSER+".staxEndElement(): </description> found:adding desc");
			if(content == null) {
				System.out.println(STAXPARSER+".staxEndElement(): store "+sharedDataObj.SORRY_INFO);
				content = sharedDataObj.SORRY_INFO;
			}//end if
			newsItem.setDescription(content);
			content = null;
		}else if ( stTag.equals("link") && qName.equals("link")) {
			// <link> </link>=>found
			System.out.println(STAXPARSER+".staxEndElement(): </link> found:adding link");
			newsItem.setLink(content);
			content = null;
			return;
		}else if ( stTag.equals("item") && qName.equals("item")) {
			//<item> </item> => found
			System.out.println(STAXPARSER+".staxEndElement(): </item> found:adding node");
			cList.addToList(newsItem);
			content = null;
			if(startTagStack.isEmpty()) {
				System.out.println(STAXPARSER+".staxEndElement():<item>... </item> PARSING WENT FINE");
			}
		}else {
			System.out.println(STAXPARSER+".staxEndElement(): ENI <item> child Element not interested");
		}
	}//staxEndElement(XMLStreamReader rEvent)
	
	/************************************************************************************************/
}
