import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public enum ItemsListDB {
	WORLD, TECH, SPORTS, TOPSTORY;
	
	private List <NewsItem> itemsLst=null;
	@SuppressWarnings("unused")
	private NewsItem item;

	public void createList() {
		System.out.println("ItemsListDB.createList():called");
		itemsLst = new LinkedList<NewsItem>();
	}
	
	public NewsItem getNewsItem() {
		System.out.println("ItemsListDB.getNewsItem():get an element to be added to the list");
		return new NewsItem();
	}
	
	public void addToList(NewsItem n) {
		System.out.println("ItemsListDB.getNewsItem():add the item to the list");
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
