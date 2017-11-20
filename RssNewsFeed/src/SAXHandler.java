import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler{
	private final String SAXHANDLER = this.getClass().getSimpleName();
	
	//private ItemsListDB itemsList;
	
	public SAXHandler() {
		System.out.println(SAXHANDLER+"(): called");
	}
	
}
