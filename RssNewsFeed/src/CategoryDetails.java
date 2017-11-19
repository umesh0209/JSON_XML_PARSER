
public class CategoryDetails {
	private final String CATDET = this.getClass().getSimpleName();
	private String category;
	private ItemsListDB itemsDB;
	private String fName;

	public CategoryDetails() {
		System.out.println(CATDET+".CategoryDetails() called");
		category = null;
	}
	
	public CategoryDetails(String c, ItemsListDB i, String fNam) {
		category = c;
		itemsDB = i;
		setfName(fNam);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ItemsListDB getItemsDB() {
		return itemsDB;
	}

	public void setItemsDB(ItemsListDB itemsDB) {
		this.itemsDB = itemsDB;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}
}
