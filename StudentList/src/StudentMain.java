
public class StudentMain {

	public static void main(String[] args) {
		//Get Boys instance
		Student boys = Student.BOYS;
		Student girls = Student.GIRLS;
	
		boys.createList();
		boys.add("Umesh", 20);
		boys.add("Chai", 10);

		girls.createList();
		girls.add("Mahima", 18);
		girls.add("Tanya", 18);
		
		System.out.println("============================================");
		System.out.println("BOYS LIST IS:");
		boys.display();
		System.out.println("GIRLS LIST IS:");
		girls.display();
		System.out.println("============================================");
	}
}
