import java.io.IOException;
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
	
	private String url;
	private String cat;
	private String fileName;
	private ItemsListDB itemsDB;
	private DomParser dom=null;
	
	public static final String CBS_NEWS_RSS_FNAME = "xml/newsrss.xml";

	public RssCategoryNews() {
		System.out.println(RSSCATNEWS+"():Object Creation");
		url = cat = fileName = null;
		itemsDB = ItemsListDB.getinstance();
		dom = new DomParser();
	}//end RssCategoryNews()
	
	public void setCategory() {
		System.out.println(RSSCATNEWS+".setCategory():called");
		Scanner scan = new Scanner(System.in);
		System.out.println("News Type World(1), Tech(2), Games(3), Top Stories(4)");
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
		url = CBS_RSS_URL;
		fileName = CBS_NEWS_RSS_FNAME;
		System.out.println(RSSCATNEWS+".makeUrl():"+fileName);
		switch(cat){
			case CAT_WORLD:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_WORLD);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			case CAT_SCITECH:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_SCITECH);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);			
				}
			}
			break;
			
			case CAT_GAMECORE:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_GAMECORE);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			case CAT_TOPSTORIES:{
				if (url.contains("category")) {
					url = url.replace("category", CAT_TOPSTORIES);
					System.out.println(RSSCATNEWS+".makeUrl():url replaced by category+"+url);
				}
			}
			break;
			
			default:
				break;
		}//end switch(cat)
	}//end makeUrl()
	
	public String getFileName() {
		System.out.println(RSSCATNEWS+".getFileName():called");
		return fileName;
	}
	
	public void setFileName(String fName) {
		System.out.println(RSSCATNEWS+".setFileName():called");
		this.fileName = fName;
	}
	
	public void getXML() {
		System.out.println(RSSCATNEWS+".getXML():called");
		
		HttpHandler sh = new HttpHandler();
		// Making a request to url and getting response
		try {
			sh.setFileName(fileName);
			sh.makeServiceCall(url);
			}catch(IOException ie){
				System.out.println(ie.getMessage());
				System.exit(0);
			}
	}//end getXML()
	
	public void domParser() {
		System.out.println(RSSCATNEWS+".domParser():called");
		dom.parseXML(fileName);
	}
	
	public void displayItems() {
		System.out.println(RSSCATNEWS+".displayItems():called");
		System.out.println("**************************************CBS NEWS*******************************");
		itemsDB.display();
	}//end displayItems()
}
