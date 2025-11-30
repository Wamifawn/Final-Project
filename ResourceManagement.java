import java.io.*;
import java.util.*;


public class ResourceManagement
{
    private PriorityQueue<Department> departmentPQ;
    private Double remainingBudget;
    private Double budget;
    private List<Department> departmentsList;

    public static void printName()
    {
        System.out.println("This solution was completed by:");
        System.out.println("Mark Guzman, Garrison Lazard");
        System.out.println("Mfl906, Hub017");
    }

    public ResourceManagement(String fileNames[], Double budget)
    {
        this.budget = budget;
        this.remainingBudget = budget;
        this.departmentsList = new ArrayList<Department>();

        // *** FIXED FOR OLD JAVA ***
        // Must use: PriorityQueue(int initialCapacity, Comparator comparator)
        this.departmentPQ =
                new PriorityQueue<Department>(100, new Comparator<Department>() {
                    public int compare(Department a, Department b) {
                        int cmp = a.priority.compareTo(b.priority);
                        if (cmp != 0) return cmp;
                        return a.name.compareTo(b.name);
                    }
                });

        // Load all departments
        for (int i = 0; i < fileNames.length; i++) {
            Department d = new Department(fileNames[i]);
            if (d.name != null) {
                departmentPQ.add(d);
                departmentsList.add(d);
            }
        }

        System.out.println("");
        System.out.println("ITEMS PURCHASED");
        System.out.println("----------------------------");

        // Purchasing loop
        while (remainingBudget > 0 && !departmentPQ.isEmpty()) {

            Department dept = departmentPQ.poll();

            // Move unaffordable items to removed
            while (!dept.itemsDesired.isEmpty() &&
                    dept.itemsDesired.peek().price > remainingBudget) {
                dept.itemsRemoved.add(dept.itemsDesired.poll());
            }

            if (dept.itemsDesired.isEmpty()) {

                if (remainingBudget > 0) {
                    double scholarshipAmount = Math.min(1000.0, remainingBudget);
                    Item scholarship = new Item("Scholarship", scholarshipAmount);

                    dept.itemsReceived.add(scholarship);
                    dept.priority += scholarshipAmount;
                    remainingBudget -= scholarshipAmount;

                    String price = String.format("$%.2f", scholarshipAmount);
                    System.out.printf(
                            "Department of %-30s- %-30s- %30s\n",
                            dept.name, scholarship.name, price
                    );
                }

            } else {
                Item buy = dept.itemsDesired.poll();

                dept.itemsReceived.add(buy);
                dept.priority += buy.price;
                remainingBudget -= buy.price;

                String price = String.format("$%.2f", buy.price);
                System.out.printf(
                        "Department of %-30s- %-30s- %30s\n",
                        dept.name, buy.name, price
                );
            }

            departmentPQ.add(dept);

            if (remainingBudget <= 0.000001) {
                remainingBudget = 0.0;
                break;
            }
        }

        System.out.println("");
    }

    public void printSummary()
    {
        // Sort summary by total spent
        List<Department> sorted = new ArrayList<Department>(departmentsList);
        Collections.sort(sorted, new Comparator<Department>() {
            public int compare(Department a, Department b) {
                int cmp = a.priority.compareTo(b.priority);
                if (cmp != 0) return cmp;
                return a.name.compareTo(b.name);
            }
        });

        for (Department dept : sorted) {

            System.out.println("Department of " + dept.name);

            String totalSpent = String.format("$%.2f", dept.priority);
            double percent = 0.0;
            if (budget != 0.0)
                percent = (dept.priority / budget) * 100.0;

            System.out.printf("Total Spent       = %10s\n", totalSpent);
            System.out.printf("Percent of Budget = %6.2f%%\n", percent);
            System.out.println("----------------------------");

            System.out.println("ITEMS RECEIVED");
            for (Item it : dept.itemsReceived) {
                String price = String.format("$%.2f", it.price);
                System.out.printf("%-30s - %30s\n", it.name, price);
            }
            System.out.println("");

            System.out.println("ITEMS NOT RECEIVED");

            Queue<Item> notReceived = new LinkedList<Item>();
            notReceived.addAll(dept.itemsRemoved);
            notReceived.addAll(dept.itemsDesired);

            for (Item it : notReceived) {
                String price = String.format("$%.2f", it.price);
                System.out.printf("%-30s - %30s\n", it.name, price);
            }

            System.out.println("");
        }
    }
}

class Department
{
    String name;
    Double priority;
    Queue<Item> itemsDesired;
    Queue<Item> itemsReceived;
    Queue<Item> itemsRemoved;

    public Department(String fileName)
    {
        this.priority = 0.0;
        this.itemsDesired = new LinkedList<Item>();
        this.itemsReceived = new LinkedList<Item>();
        this.itemsRemoved = new LinkedList<Item>();

        File file = new File(fileName);
        Scanner input = null;

        try {
            input = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The file " + fileName + " was not found.");
            return;
        }

        if (!input.hasNext()) {
            input.close();
            return;
        }

        this.name = input.next();

        while (input.hasNext()) {
            String itemName = input.next();
            if (!input.hasNextDouble()) break;

            double price = input.nextDouble();
            itemsDesired.add(new Item(itemName, price));
        }

        input.close();
    }
}

class Item
{
    String name;
    Double price;

    public Item(String name, Double price)
    {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return "{ " + name + ", " + price + " }";
    }
}
