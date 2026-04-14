public class Lab {
    private String lab;
    private int cost;

    public Lab(String lab, int cost) {
        this.lab = lab;
        this.cost = cost;
    }

    public static Lab newLab(java.util.Scanner sc) {
        System.out.println("---- Add New Lab ----");
        String name = InputUtil.readNonEmptyString(sc, "Lab Name: ");
        int cost = InputUtil.readInt(sc, "Cost: ");
        return new Lab(name, cost);
    }

    public void showLab() {
        System.out.printf("%-20s Cost:%-6d%n", lab, cost);
    }

    public void labList() {
        showLab();
    }

    public String toString() {
        return String.format("%-20s Cost:%-6d", lab, cost);
    }

    public String getLab() { return lab; }
    public int getCost() { return cost; }
}
