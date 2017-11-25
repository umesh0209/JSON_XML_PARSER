public interface IXmlFunctions {
	
	//These functions are implemented by DomParser class
	//public void makeURL();
	public void setNextParseMethod(IXmlFunctions next);
	public void processMethodHanlder(String className);
}
