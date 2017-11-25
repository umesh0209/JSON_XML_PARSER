
public abstract class XmlFunctions implements IXmlFunctions{
	private final String XMLFUNC = this.getClass().getSimpleName();
	
	private IXmlFunctions nxtProcess;
	
	public void setNextParseMethod(IXmlFunctions next) {
		System.out.println(XMLFUNC+".setNextParseMethod():called");
		nxtProcess = next;
	}
	
	public void processMethodHanlder(String className) {
		System.out.println(XMLFUNC+".processMethodHanlder():called");
		
		String parsingClassName = getParsingClassString();
		System.out.println(XMLFUNC+".processMethodHanlder():parsingClassName="+parsingClassName);
		if (parsingClassName.equals(className)) {
			System.out.println(XMLFUNC+".processMethodHanlder(): start parsing...");
			parsingXML();
		}
		else {
			System.out.println(XMLFUNC+".processMethodHanlder(): forward to next in the chain...");
			nxtProcess.processMethodHanlder(className);
		}
	}
	
	public static void processParseMethod(String clsMethodStr) {
		System.out.println("XmlFunctions.processParseMethod():called");
		//Create xml process objects
		System.out.println("XmlFunctions.processParseMethod():creating xml process objects...");
		SharedData sharedDataObj = SharedData.getInstance();
		String url = sharedDataObj.getUrlWithCat();
		IXmlFunctions domProcess = new DomParser();
		IXmlFunctions saxProcess = new SAXXMLParser(url);
		IXmlFunctions staxProcess = new StAXParser(url);
		
		//Chain them together...
		System.out.println("XmlFunctions.processParseMethod():Chaining them together...");
		domProcess.setNextParseMethod(saxProcess);
		saxProcess.setNextParseMethod(staxProcess);
		
		//start the ball rolling...
		System.out.println("XmlFunctions.processParseMethod():start the ball rolling...");
		domProcess.processMethodHanlder(clsMethodStr);
		
	}
	
	protected abstract void parsingXML();
	protected abstract String getParsingClassString();	
}
