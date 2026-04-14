public class Nurse extends Staff {
    private String department;


    public Nurse(String id, String name, String sex, int salary, String department) {
        super(id, name, "Nurse", sex, salary);
        this.department = department;
    }


    public void showStaffInfo() {

        System.out.printf("%-6s %-15s %-12s %-4s %-7d Dept:%-12s%n",
                id, name, designation, sex, salary, department);
    }


    public String toString() {
        return String.format("%-6s %-15s %-12s %-4s %-7d Dept:%-12s",
                id, name, designation, sex, salary, department);
    }

    public String getDepartment() { return department; }
}
