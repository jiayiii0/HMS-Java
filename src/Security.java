public class Security extends Staff {
    private String shift;

    public Security(String id, String name, String sex, int salary, String shift) {
        super(id, name, "Security", sex, salary);
        this.shift = shift;
    }

 
    public void showStaffInfo() {
     
        System.out.printf("%-6s %-15s %-12s %-4s %-7d Shift:%-12s%n",
                id, name, designation, sex, salary, shift);
    }


    public String toString() {
        return String.format("%-6s %-15s %-12s %-4s %-7d Shift:%-12s",
                id, name, designation, sex, salary, shift);
    }

    public String getShift() { return shift; }
}
