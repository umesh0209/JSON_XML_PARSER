
public class CbsRssMain {

	public CbsRssMain() {
	}

	public static void main(String[] args) {
		System.out.println("main():called");

		RssCategoryNews cbsRssObj = new RssCategoryNews();
		cbsRssObj.setCategory();
		cbsRssObj.makeUrl();
		cbsRssObj.doParse();
		cbsRssObj.displayItems();
	}
}
