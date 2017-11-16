import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StdList{
	private static final String STDLIST= "StdList";
	private List<StdDetails> list;
	
	public StdList(){
		System.out.println(STDLIST+"() called");
		list = new LinkedList<StdDetails>();
	}
	
	public void add(String s, int a) {
		System.out.println(STDLIST+".add() called");
		StdDetails std = new StdDetails(s,a);
		list.add(std);
	}
	
	public void display() {
		//System.out.println(STDLIST+".display() called");
		ListIterator<StdDetails> iter = list.listIterator();
		
		while (iter.hasNext()) {
			StdDetails s = iter.next();
			System.out.println(s.toString());
		}
	}
}