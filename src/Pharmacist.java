public class Pharmacist extends Staff {
    private String license;

    public Pharmacist(String id, String name, String sex, int salary, String license) {
        super(id, name, "Pharmacist", sex, salary);
        this.license = license;
    }


    public void showStaffInfo() {
     
        System.out.printf("%-6s %-15s %-12s %-4s %-7d License:%-14s%n",
                id, name, designation, sex, salary, license);
    }


    public String toString() {
        return String.format("%-6s %-15s %-12s %-4s %-7d License:%-14s",
                id, name, designation, sex, salary, license);
    }

    public String getLicense() { return license; }
}
