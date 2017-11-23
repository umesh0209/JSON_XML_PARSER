import java.io.IOException;
import java.util.Map;

abstract class XmlFunctions implements IXmlFunctions{
	public abstract void makeURL();
	public abstract void doParse();
}

public class XmlParser extends XmlFunctions {
	
	private final String XMLPARSER = this.getClass().getSimpleName();

	private String url;
	private SharedData sharedDataObj = SharedData.getInstance();
	private int type;
	private Map<Integer,CategoryDetails> catList;

	public XmlParser() {
		System.out.println(XMLPARSER+"():called");
	}
	
	@Override
	public void makeURL() {
		System.out.println(XMLPARSER+".makeURL():called");
		
		setContext();
		System.out.println(XMLPARSER+".makeURL():Extract category string");
		String catVal = catList.get(type).getCategory();
		System.out.println(XMLPARSER+".makeURL():User selected:"+ catVal);
		
		if (url.contains("category")) {
			url = url.replace("category", catVal);
			System.out.println(XMLPARSER+".makeUrl():url replaced by category+"+url);
		}
	}//end makeUrl()
	
	@Override
	public void doParse()
	{
		System.out.println(XMLPARSER+".parseMethod():called");
		
		int method = sharedDataObj.getMethod();
		if (method == sharedDataObj.XML_METHOD_DOM ) {
			getXML();
			DomParser domParserObj = new DomParser();
			domParserObj.doParse();
		}
		else if (method == sharedDataObj.XML_METHOD_SAX ) {
			SAXXMLParser saxParserObj = new SAXXMLParser(url);
			saxParserObj.doParse();
		}
	}
	
	/*************************************************************************************
	 *                                 PRIVATE FUNCTIONS                                 *
	 ************************************************************************************/
	private void getXML() {
		System.out.println(XMLPARSER+".getXML():called");
		
		HttpHandler sh = new HttpHandler();
		// Making a request to url and getting response
		
		try {
				sh.makeServiceCall(url);
			}catch(IOException ie){
				System.out.println(ie.getMessage());
				System.exit(0);
			}
	}//end getXML()
	
	private void setContext() {
		System.out.println(XMLPARSER+".setContext():private func: Set the xml context");
		url = sharedDataObj.getURL();
		catList = sharedDataObj.getCatList();
		type = sharedDataObj.getType();
	}
	/************************************************************************************/
}

