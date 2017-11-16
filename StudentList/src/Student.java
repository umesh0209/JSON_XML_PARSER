public enum Student{
	BOYS, GIRLS;
	
	private StdList lst;
	
	public void createList() {
		System.out.println("Student.createList():called");
		lst = new StdList();
	}
	
	public void add(String s, int a) {
		System.out.println("Student.add():called");
		lst.add(s, a);
	}
	
	public void display() {
		//System.out.println("Student.display():called");
		lst.display();
	}
}

