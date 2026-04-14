public abstract class Staff {
    protected String id;
    protected String name;
    protected String designation;
    protected String sex;
    protected int salary;

    public Staff(String id, String name, String designation, String sex, int salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.sex = sex;
        this.salary = salary;
    }

    protected static String[] collectBaseFields(java.util.Scanner sc) {
        String id = InputUtil.readNonEmptyString(sc, "ID: ");
        String name = InputUtil.readNonEmptyString(sc, "Name: ");
        String sex = InputUtil.readNonEmptyString(sc, "Sex: ");
        int salary = InputUtil.readInt(sc, "Salary: ");
        return new String[]{id, name, sex, String.valueOf(salary)};
    }
 
    public static Staff newStaff(java.util.Scanner sc) {
        System.out.println("---- Add New Staff ----");
        String[] base = collectBaseFields(sc);
        String id = base[0];
        String name = base[1];
        String sex = base[2];
        int salary = Integer.parseInt(base[3]);

        String role = InputUtil.readNonEmptyString(sc, "Role (Nurse/Pharmacist/Security): ");
        if (role.equalsIgnoreCase("Nurse")) {
            String dept = InputUtil.readNonEmptyString(sc, "Department: ");
            return new Nurse(id, name, sex, salary, dept);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            String license = InputUtil.readNonEmptyString(sc, "License: ");
            return new Pharmacist(id, name, sex, salary, license);
        } else if (role.equalsIgnoreCase("Security")) {
            String shift = InputUtil.readNonEmptyString(sc, "Shift: ");
            return new Security(id, name, sex, salary, shift);
        } else {
            System.out.println("Unknown role, default to Security.");
            String shift = InputUtil.readNonEmptyString(sc, "Shift: ");
            return new Security(id, name, sex, salary, shift);
        }
    }

    public void showBaseInfo() {
        System.out.printf("%-6s %-15s %-12s %-6s %-7d", id, name, designation, sex, salary);
    }

    public abstract void showStaffInfo();

  
    public String toString() {
        return String.format("%-6s %-15s %-12s %-4s %-7d", id, name, designation, sex, salary);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public String getSex() { return sex; }
    public int getSalary() { return salary; }
}
