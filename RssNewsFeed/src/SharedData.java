import java.util.HashMap;
import java.util.Map;

public class SharedData {

	/***************************SINGLETON DATA OBJ*********************************/
	private final String SHRDDATA = this.getClass().getSimpleName();
	private static SharedData instance=null;
	
	private SharedData() {
	}
	
	public synchronized static SharedData getInstance() {
		System.out.println("SharedData.getInstance() singleton function called");
		if (instance == null) {
			instance = new SharedData();
		}
		return instance;
	}
	/**********************************************************************************************/
	
	/***********************************************************************************************
	 *                        SHARED DATA OBJ PRIVATE/PUBLIC DATA/FUNCTIONS                        *
	 ***********************************************************************************************/

	// public constants
	public final int XML_METHOD_DOM = 1;
	public final int XML_METHOD_SAX = 2;
	public final int XML_METHOD_StAX = 3;
	public final String SORRY_INFO="########Sorry, No details found, please check the link ######";
	public final String SORRY_LINK="########Sorry, No link found please go to https://www.cbsnews.com/ ######";
	
	//private data 
	private Map<Integer, CategoryDetails> catList = new HashMap<>();
	private  final String CBS_RSS_URL = 
			"https://www.cbsnews.com/latest/rss/category";
	private int type;
	private int method;
	
	//public functions
	public void createCatList() {
		System.out.println(SHRDDATA+".createCatList() called");
		catList.put(1, new CategoryDetails("world",ItemsListDB.WORLD,"xml/newsrss.xml"));
		catList.put(2, new CategoryDetails("tech",ItemsListDB.TECH,"xml/newsrss.xml"));
		catList.put(3, new CategoryDetails("scitech-gamecore",ItemsListDB.SPORTS,"xml/newsrss.xml"));
		catList.put(4, new CategoryDetails("main",ItemsListDB.TOPSTORY,"xml/newsrss.xml"));
	}
	
	public String getURL() {
		System.out.println(SHRDDATA+".getURL():called");
		return CBS_RSS_URL;
	}

	public Map<Integer, CategoryDetails> getCatList() {
		System.out.println(SHRDDATA+".getCatList():called");
		return catList;
	}
	
	public int getType() {
		System.out.println(SHRDDATA+".getType():called");
		return type;
	}

	public void setType(int type) {
		System.out.println(SHRDDATA+".getType():called");
		this.type = type;
	}

	public int getMethod() {
		System.out.println(SHRDDATA+".getMethod():called");
		return method;
	}

	public void setMethod(int method) {
		System.out.println(SHRDDATA+".setMethod():called");
		this.method = method;
	}
	/**********************************************************************************************/
}
