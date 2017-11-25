import java.io.IOException;
import java.util.Map;

public class XmlParser{
	
	private final String XMLPARSER = this.getClass().getSimpleName();

	private String url;
	private SharedData sharedDataObj = SharedData.getInstance();
	private int type;
	private Map<Integer,CategoryDetails> catList;

	public XmlParser() {
		System.out.println(XMLPARSER+"():called");
	}
	
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
		sharedDataObj.setUrlWithCat(url);
	}//end makeUrl()
	
	public void doParse()
	{
		System.out.println(XMLPARSER+".doParse():called");
		String parsingClassName=null;
		
		int method = sharedDataObj.getMethod();
		if (method == sharedDataObj.XML_METHOD_DOM ) {
			getXML();
			parsingClassName = DomParser.class.getName().toString();
		}
		else if (method == sharedDataObj.XML_METHOD_SAX ) {
			parsingClassName = SAXXMLParser.class.getName().toString();
		}
		else if (method == sharedDataObj.XML_METHOD_StAX) {
			parsingClassName = StAXParser.class.getName().toString();
		}
		else {
			System.out.println("Please check the input for method.....");
			System.exit(0);
		}
		
		System.out.println(XMLPARSER+".doParse():parsingClassName="+parsingClassName);
		XmlFunctions.processParseMethod(parsingClassName);
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

