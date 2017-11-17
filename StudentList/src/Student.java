public enum Student{
	BOYS, GIRLS;
	
	private StdList lst=null;
	
	public void add(String s, int a) {
		System.out.println("Student.add():called");
		if (lst == null) {
			lst = new StdList();
		}
		lst.add(s, a);
	}
	
	public void display() {
		//System.out.println("Student.display():called");
		lst.display();
	}
}

