import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ItemsListDB {
	
	private final String ITEMSLIST = this.getClass().getSimpleName();
	private List <NewsItem> itemsLst;
	@SuppressWarnings("unused")
	private NewsItem item;
	
	private static ItemsListDB instance=null;
	
	public synchronized static ItemsListDB getinstance() {
		System.out.println("ItemsListDB: Instance creation");
		if (instance == null) {
			instance = new ItemsListDB();
		}
		return instance;
	}
	
	/*public enum instance{
		
	}*/
	
	private ItemsListDB() {
		System.out.println(ITEMSLIST+"(): Object creation");
		itemsLst = new LinkedList<NewsItem>();
		item = null;
	}
	
	public NewsItem getNewsItem() {
		System.out.println(ITEMSLIST+".getNewsItem():get an element to be added to the list");
		return new NewsItem();
	}
	
	public void addToList(NewsItem n) {
		System.out.println(ITEMSLIST+".getNewsItem():add the item to the list");
		itemsLst.add(n);
	}
	
	public void display() {
		ListIterator<NewsItem> iter = itemsLst.listIterator();
		
		while (iter.hasNext()) {
			String title, desc, link;
			
			NewsItem i = iter.next();
			title = i.getTitle();
			desc = i.getDescription();
			link = i.getLink();
			
			if ( title != null) {
				System.out.println("===========================================================");
				System.out.println("title="+title);
			}//end if
			
			if (desc != null) {
				System.out.println("description="+desc);
			}//end if
			
			if (link != null) {
				System.out.println("link="+link);
			}//end if
		}//end while(iter.hasNext())
	}
}
