public class Department{
	String name = "";
	double priority = 0.0;
	List itemsDesired = new List();
	ArrayList itemsRecieved = new ArrayList();
	ArrayList itemsRemoved = new ArrayList();
	
	public Department(String name, double priority, List itemsDesired, ArrayList itemsRecieved, ArrayList itemsRemoved) {
		this.name = name;
		this.priority = priority;
		this.itemsDesired = itemsDesired;
		this.itemsRecieved = itemsRecieved;
		this.itemsRemoved = itemsRemoved;
	}
}
