import java.util.Map;

public interface IXmlFunctions {
	
	//These functions are implemented by DomParser class
	public void parseDOM();
	public void getXML();
	public void makeURL();
	public void setCatList(Map<Integer,CategoryDetails> c);
	public void setCategory(int c);
}
