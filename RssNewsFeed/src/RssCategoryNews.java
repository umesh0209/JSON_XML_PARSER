import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RssCategoryNews {
	private final String RSSCATNEWS = this.getClass().getSimpleName();
	
	public static final String CBS_RSS_URL = 
			"https://www.cbsnews.com/latest/rss/category";
	
	private String cat;
	private ItemsListDB itemsDB;
	private IDomFunctions dom=null;
	private Map<Integer, CategoryDetails> catList = new HashMap<>();
	
	public RssCategoryNews() {
		System.out.println(RSSCATNEWS+"():Object Creation");
		cat = null;
		dom = new DomParser();
	}//end RssCategoryNews()
	
	public String getCategory() {
		System.out.println(RSSCATNEWS+".getCategory():called");
		return cat;
	}
	
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
		
		createCatList();
		dom.setCatList(catList);
		dom.setCategory(type);
		itemsDB = catList.get(type).getItemsDB();
	}//end setCategory()
	
	public void makeUrl() {
		System.out.println(RSSCATNEWS+".makeUrl():called");
		dom.makeURL();

	}//end makeUrl()
	
	public void getXML() {
		System.out.println(RSSCATNEWS+".getXML():called");
		dom.getXML();
	}//end getXML()
	
	public void domParser() {
		System.out.println(RSSCATNEWS+".domParser():called");
		dom.parseXML();
	}
	
	public void displayItems() {
		System.out.println(RSSCATNEWS+".displayItems():called");
		System.out.println("**************************************CBS NEWS*******************************");
		itemsDB.display();
	}//end displayItems()
	
	/****************************************************************************************
	 *                                  PRIVATE FUNCTIONS                                   *
	 ****************************************************************************************/
	
	private void createCatList() {
		System.out.println(RSSCATNEWS+".createCatList() called");
		catList.put(1, new CategoryDetails("world",ItemsListDB.WORLD,"xml/newsrss.xml"));
		catList.put(2, new CategoryDetails("tech",ItemsListDB.TECH,"xml/newsrss.xml"));
		catList.put(3, new CategoryDetails("scitech-gamecore",ItemsListDB.SPORTS,"xml/newsrss.xml"));
		catList.put(3, new CategoryDetails("main",ItemsListDB.TOPSTORY,"xml/newsrss.xml"));
	}

}
