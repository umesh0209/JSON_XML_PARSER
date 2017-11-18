import java.util.Scanner;

public class RssCategoryNews {
	private final String RSSCATNEWS = this.getClass().getSimpleName();
	
	public static final String CBS_RSS_URL = 
			"https://www.cbsnews.com/latest/rss/category";
	
	/* News Categories */
	public static final String CAT_WORLD = "world";
	public static final String CAT_SCITECH="tech";
	public static final String CAT_GAMECORE="scitech-gamecore";
	public static final String CAT_TOPSTORIES="main";
	
	private String cat;
	private ItemsListDB itemsDB;
	private DomParser dom=null;
	
	public static final String CBS_NEWS_RSS_FNAME = "xml/newsrss.xml";

	public RssCategoryNews() {
		System.out.println(RSSCATNEWS+"():Object Creation");
		cat = null;
		itemsDB = ItemsListDB.getinstance();
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
		
		switch(type) {
			case 1:
			{
				this.cat = CAT_WORLD;
			}
			break;
			
			case 2:{
				this.cat = CAT_SCITECH;
			}
			break;
			
			case 3:{
				this.cat = CAT_GAMECORE;
			}
			break;
			
			case 4:{
				this.cat = CAT_TOPSTORIES;
			}
			break;
			
			default:{
				System.out.println("Wrong input, try again");
				System.exit(0);
			}
		}//end switch
	}//end setCategory()
	
	public void makeUrl() {
		System.out.println(RSSCATNEWS+".makeUrl():called");
		dom.makeURL(cat);

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
}
