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
		if (type <0 || type > 5) {
			System.out.println(RSSCATNEWS+"..setCategory()Wrong input, try again");
			System.exit(0);
		}
		
		System.out.println("Method for Parsing: DOM(1), SAX(2), StAX(3)");
		int method = scan.nextInt();
		scan.close();
		
		if (type <0 || type > 4) {
			System.out.println(RSSCATNEWS+"..setCategory()Wrong input, try again");
			System.exit(0);
		}
		
		sharedDataObj.createCatList();
		sharedDataObj.setType(type);
		sharedDataObj.setMethod(method);
		
	}//end setCategory()
	
	public void makeUrl() {
		System.out.println(RSSCATNEWS+".makeUrl():called");
		xmlObj.makeURL();

	}//end makeUrl()
	
	public void doParse() {
		System.out.println(RSSCATNEWS+".doParse():called");
		xmlObj.doParse();
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
