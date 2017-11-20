import java.util.Scanner;

public class RssCategoryNews {
	private final String RSSCATNEWS = this.getClass().getSimpleName();
	
	private IXmlFunctions xmlObj=null;
	private SharedData sharedDataObj = SharedData.getInstance();
	
	public RssCategoryNews() {
		System.out.println(RSSCATNEWS+"():Object Creation");
		xmlObj = new XmlParser();
	}//end RssCategoryNews()
	
	public void setCategory() {
		System.out.println(RSSCATNEWS+".setCategory():called");
		Scanner scan = new Scanner(System.in);
		System.out.println("News Type World(1), Tech(2), Sports(3), Top Stories(4)");
		int type = scan.nextInt();
		scan.close();
		
		if (type <0 || type > 5) {
			System.out.println(RSSCATNEWS+"..setCategory()Wrong input, try again");
			System.exit(0);
		}
		
		sharedDataObj.createCatList();
		sharedDataObj.setType(type);
		sharedDataObj.setMethod(sharedDataObj.XML_METHOD_DOM);
		
	}//end setCategory()
	
	public void makeUrl() {
		System.out.println(RSSCATNEWS+".makeUrl():called");
		xmlObj.makeURL();

	}//end makeUrl()
	
	public void getXML() {
		System.out.println(RSSCATNEWS+".getXML():called");
		xmlObj.getXML();
	}//end getXML()
	
	public void parsingXML() {
		System.out.println(RSSCATNEWS+".domParser():called");
		xmlObj.parseMethod();
	}
	
	public void displayItems() {
		int type;
		type = sharedDataObj.getType();
		ItemsListDB itemsDB = sharedDataObj.getCatList().get(type).getItemsDB();
		System.out.println(RSSCATNEWS+".displayItems():called");
		System.out.println("**************************************CBS NEWS*******************************");
		itemsDB.display();
	}//end displayItems()
}
