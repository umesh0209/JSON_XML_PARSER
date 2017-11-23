import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXXMLParser{
	private final String SAXXMLPARSER = this.getClass().getSimpleName();
	private String url;
	
	public SAXXMLParser(String url) {
		System.out.println(SAXXMLPARSER+"("+url+"):called");
		this.url = url;
	}
	
	public void doParse() {
		System.out.println(SAXXMLPARSER+".doParse():called");
		
		URL ser_url=null;
		try {
			ser_url = new URL(url);
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXHandler handler = new SAXHandler();
			InputStream is = ser_url.openStream();
			//InputStream is = new FileInputStream("xml/temp.xml");
			parser.parse(is, handler);
		}catch(MalformedURLException me) {
			me.printStackTrace();
			System.exit(0);
		} catch (ParserConfigurationException pe) {
			pe.printStackTrace();
			System.exit(0);
		} catch (SAXException se) {
			se.printStackTrace();
			System.exit(0);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(0);
		}
	}
}

final class SAXHandler extends DefaultHandler{
	
	private final String SAXHANDLER = this.getClass().getSimpleName();
	
	private SharedData sharedDataObj = SharedData.getInstance();
	private NewsItem newsItem;
	private ItemsListDB cList;
	private String content;
	int type;
	Stack<String> startTagStack;
		
	public SAXHandler() {
		System.out.println(SAXHANDLER+"(): called");
		type = sharedDataObj.getType();
		cList = sharedDataObj.getCatList().get(type).getItemsDB();
		startTagStack = new Stack<String>();
	}//SAXHandler()
	
	@Override
	public void startElement(String uri, String localName, String qName, 
								Attributes attributes) throws SAXException {
		
		System.out.println(SAXHANDLER+".startElement(): called");
		System.out.println(SAXHANDLER+".startElement(): qName="+ qName +" found");
		
		if (qName.equals("channel")){
			System.out.println(SAXHANDLER+".startElement(): <channel> found, items will follow...");
			cList.createList();
			return;
		}else if (qName.equals("item")) {
			System.out.println(SAXHANDLER+".startElement(): <item> found, create an node");
			newsItem = cList.getNewsItem();
			startTagStack.push("item");
			return;
		}
		
		//Before proceeding further check the stack, the length of the stack should be always max 2,
		// top=item related elements like title, desc, link, and bottom should be always <item> tag
		//for any other element ignore
		if (startTagStack.isEmpty()) {
			System.out.println(SAXHANDLER+".startElement(): Element not related to <item>");
			return;
		}
		
		if (qName.equals("title")) {
			System.out.println(SAXHANDLER+".startElement(): <title> found");
			startTagStack.push("title");
			content = null;
		}else if (qName.equals("description")) {
			System.out.println(SAXHANDLER+".startElement(): <description> found");
			startTagStack.push("description");
			content = null;
		}else if (qName.equals("link")) {
			System.out.println(SAXHANDLER+".startElement(): <link> found");
			startTagStack.push("link");
			content = null;
		}else {
			System.out.println(SAXHANDLER+".startElement(): <item> child element not ineteresed(ENI)");
			//Store it as Element Not interested (ENI) which are <image> <pubDate> <guid> of <item>
			startTagStack.push("ENI");
		}
	}// startElement()

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		System.out.println(SAXHANDLER+".characters(): contents="+content);
		
	    //if stack is empty then the content is related to <desc,title,link>, of <item> so ignore
	    if(startTagStack.isEmpty()) {
	    	System.out.println(SAXHANDLER+".characters():IGNORED");
	    	content = null;
	    	return;
	    }
	    
	    if (startTagStack.peek().equals("ENI") == false) {
		    String buffer = String.copyValueOf(ch, start, length).trim();
		    if(content == null ) {
		    	content = buffer;
		    }else {
		    	content = content.concat(buffer);
		    }
	    }
	} //end of characters()
	
	@Override
	public void endElement(String uri, String localName,
					String qName) throws SAXException {
		System.out.println(SAXHANDLER+".endElement(): called");
		String stTag=null;
		
		//This will happen when no <item> tag is added to stack
		if ( startTagStack.isEmpty() ) {
			System.out.println(SAXHANDLER+".endElement(): element not related to <item>, so ignore");
			return;
		}
		stTag = startTagStack.pop();

		// <title> </title>=>found
		if ( stTag.equals("title") && qName.equals("title")) {
				System.out.println(SAXHANDLER+".endElement():</title> found: adding title");
				newsItem.setTitle(content);
				content = null;
		}else if ( stTag.equals("description") && qName.equals("description")) {
			// <description> </description>=>found
			System.out.println(SAXHANDLER+".endElement(): </description> found:adding desc");
			if(content == null) {
				System.out.println(SAXHANDLER+".endElement(): store "+sharedDataObj.SORRY_INFO);
				content = sharedDataObj.SORRY_INFO;
			}//end if
			newsItem.setDescription(content);
			content = null;
		}else if ( stTag.equals("link") && qName.equals("link")) {
			// <link> </link>=>found
			System.out.println(SAXHANDLER+".endElement(): </link> found:adding link");
			newsItem.setLink(content);
			content = null;
			return;
		}else if ( stTag.equals("item") && qName.equals("item")) {
			//<item> </item> => found
			System.out.println(SAXHANDLER+".endElement(): </item> found:adding node");
			cList.addToList(newsItem);
			content = null;
			if(startTagStack.isEmpty()) {
				System.out.println(SAXHANDLER+".endElement():<item>... </item> PARSING WENT FINE");
			}
		}else {
			System.out.println(SAXHANDLER+".endElement(): ENI(<item> child Element not interested");
		}
	}//endElement()
}
