package AS3;


	import java.util.*;

	// ---------------------------
	// Resource
	// ---------------------------
	class Resource {
	    protected String name;
	    protected double quantity;
	    protected double costPerUnit;

	    public Resource(String name, double quantity, double costPerUnit) {
	        this.name = name;
	        this.quantity = quantity;
	        this.costPerUnit = costPerUnit;
	    }

	    public double getCost() {
	        return quantity * costPerUnit;
	    }

	    @Override
	    public String toString() {
	        return name + " (" + quantity + " × " + costPerUnit + ") = $" + String.format("%.2f", getCost());
	    }
	}

	// ---------------------------
	// Vehicle / AGV
	// ---------------------------
	class Vehicle {
	    protected String id;
	    protected String type;

	    public Vehicle(String id, String type) {
	        this.id = id;
	        this.type = type;
	    }

	    public void performTask(String task) {
	        System.out.println(type + " (ID: " + id + ") performing: " + task);
	    }
	}

	class AGV extends Vehicle {
	    private double hourlyCost;
	    private double hoursUsed;

	    public AGV(String id, String type, double hourlyCost, double hoursUsed) {
	        super(id, type);
	        this.hourlyCost = hourlyCost;
	        this.hoursUsed = hoursUsed;
	    }

	    public double getCost() {
	        return hourlyCost * hoursUsed;
	    }

	    @Override
	    public String toString() {
	        return type + " (ID: " + id + ") cost = $" + String.format("%.2f", getCost());
	    }
	}

	// ---------------------------
	// Operations
	// ---------------------------
	abstract class Operation {
	    protected String name;
	    protected List<Resource> resources = new ArrayList<>();
	    protected List<AGV> agvs = new ArrayList<>();

	    public Operation(String name) {
	        this.name = name;
	    }

	    public void addResource(Resource r) {
	        resources.add(r);
	    }

	    public void addAGV(AGV a) {
	        agvs.add(a);
	    }

	    public double calculateCost() {
	        double total = 0.0;
	        for (Resource r : resources) total += r.getCost();
	        for (AGV a : agvs) total += a.getCost();
	        return total;
	    }

	    public abstract void execute();

	    public void printCostDetails() {
	        System.out.println("  Operation: " + name);
	        for (Resource r : resources) System.out.println("    Resource -> " + r);
	        for (AGV a : agvs) System.out.println("    AGV -> " + a);
	        System.out.println("    Operation cost = $" + String.format("%.2f", calculateCost()));
	    }
	}

	class LoadingOperation extends Operation {
	    public LoadingOperation(String name) { super(name); }
	    public void execute() {
	        System.out.println("Executing " + name + " (Loading)...");
	        for (AGV agv : agvs) agv.performTask("Load materials");
	    }
	}

	class TransportOperation extends Operation {
	    public TransportOperation(String name) { super(name); }
	    public void execute() {
	        System.out.println("Executing " + name + " (Transport)...");
	        for (AGV agv : agvs) agv.performTask("Move items across warehouse");
	    }
	}

	class SortingOperation extends Operation {
	    public SortingOperation(String name) { super(name); }
	    public void execute() {
	        System.out.println("Executing " + name + " (Sorting)...");
	        for (AGV agv : agvs) agv.performTask("Sort items");
	    }
	}

	// ---------------------------
	// Industrial Process
	// ---------------------------
	class IndustrialProcess {
	    private String name;
	    private List<Operation> operations = new ArrayList<>();

	    public IndustrialProcess(String name) { this.name = name; }

	    public void addOperation(Operation op) { operations.add(op); }

	    public double getTotalCost() {
	        double total = 0.0;
	        for (Operation op : operations) total += op.calculateCost();
	        return total;
	    }

	    public void start() {
	        System.out.println("\n--- Starting Process: " + name + " ---");
	        for (Operation op : operations) {
	            op.execute();
	            op.printCostDetails();
	        }
	        System.out.println("Total process cost: $" + String.format("%.2f", getTotalCost()));
	    }
	}

	// ---------------------------
	// Warehouse
	// ---------------------------
	class Warehouse {
	    private String name;
	    private List<IndustrialProcess> processes = new ArrayList<>();

	    public Warehouse(String name) { this.name = name; }

	    public void addProcess(IndustrialProcess p) { processes.add(p); }

	    public double calculateTotalCost() {
	        double total = 0.0;
	        for (IndustrialProcess p : processes) total += p.getTotalCost();
	        return total;
	    }

	    public void simulate() {
	        System.out.println("\n=== Simulating Warehouse: " + name + " ===");
	        for (IndustrialProcess p : processes) p.start();
	        System.out.println("\nTotal warehouse cost: $" + String.format("%.2f", calculateTotalCost()));
	    }
	}

	// ---------------------------
	// Main Class
	// ---------------------------
	public class Main {
	    public static void main(String[] args) {
	        // Resources
	        Resource electricity = new Resource("Electricity", 100, 0.5); // $50
	        Resource labor = new Resource("Labor hours", 20, 15);        // $300
	        Resource packaging = new Resource("Packaging material", 30, 2); // $60

	        // AGVs
	        AGV agv1 = new AGV("A01", "Loader AGV", 12, 10);   // $120
	        AGV agv2 = new AGV("A02", "Transport AGV", 10, 15); // $150

	        // Operations
	        LoadingOperation loadOp = new LoadingOperation("Loading Operation");
	        loadOp.addResource(electricity);
	        loadOp.addAGV(agv1);

	        TransportOperation transportOp = new TransportOperation("Transport Operation");
	        transportOp.addResource(labor);
	        transportOp.addAGV(agv2);

	        SortingOperation sortOp = new SortingOperation("Sorting Operation");
	        sortOp.addResource(packaging);
	        sortOp.addAGV(agv1);

	        // Processes
	        IndustrialProcess packagingProcess = new IndustrialProcess("Packaging Process");
	        packagingProcess.addOperation(loadOp);
	        packagingProcess.addOperation(sortOp);

	        IndustrialProcess assemblyProcess = new IndustrialProcess("Assembly Process");
	        assemblyProcess.addOperation(transportOp);

	        // Warehouse
	        Warehouse warehouse = new Warehouse("Central Distribution Center");
	        warehouse.addProcess(packagingProcess);
	        warehouse.addProcess(assemblyProcess);

	        // Simulate
	        warehouse.simulate();
	    }
	}



