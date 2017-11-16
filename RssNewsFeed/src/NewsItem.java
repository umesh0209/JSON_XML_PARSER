
public class NewsItem {
	
		private final String NEWSITEM = this.getClass().getSimpleName();

		private String title,description,link;
		
		public NewsItem() {
			System.out.println(NEWSITEM+"(): Object creation");
		}

		public String getTitle() {
			//System.out.println(NEWSITEM+".getTitle(): called");
			return title;
		}

		public void setTitle(String title) {
			System.out.println(NEWSITEM+".setTitle(): called");
			this.title = title;
		}

		public String getDescription() {
			//System.out.println(NEWSITEM+".getDescription(): called");
			return description;
		}

		public void setDescription(String description) {
			System.out.println(NEWSITEM+".setDescription(): called");
			this.description = description;
		}

		public String getLink() {
			//System.out.println(NEWSITEM+".getLink(): called");
			return link;
		}

		public void setLink(String link) {
			System.out.println(NEWSITEM+".setLink(): called");
			this.link = link;
		}
		
		@Override
		public String toString() {
			return "title="+title;
		}
}
