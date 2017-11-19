
public class CbsRss {

	public CbsRss() {
	}

	public static void main(String[] args) {
		System.out.println("main():called");

		RssCategoryNews cbsRssObj = new RssCategoryNews();
		cbsRssObj.setCategory();
		cbsRssObj.makeUrl();
		cbsRssObj.getXML();
		cbsRssObj.parsingXML();
		cbsRssObj.displayItems();
	}
}
