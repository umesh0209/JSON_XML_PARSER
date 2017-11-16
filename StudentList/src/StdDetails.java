public class StdDetails {
	private static final String STDDET= "StdDetails";
	private String Name;
	private int age;
	
	public StdDetails() {
		System.out.println(STDDET+"() called");
		Name = null;
		age = 0;
	}
	
	public StdDetails(String n, int a) {
		System.out.println(STDDET+"(param) called");
		Name = n;
		age = a;
	}

	public String getName() {
		System.out.println(STDDET+".getName() called");
		return Name;
	}

	public void setName(String name) {
		System.out.println(STDDET+".setName() called");
		Name = name;
	}

	public int getAge() {
		System.out.println(STDDET+".getAge() called");
		return age;
	}

	public void setAge(int age) {
		System.out.println(STDDET+".setAge() called");
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Name="+Name+" Age="+age;
	}
}
