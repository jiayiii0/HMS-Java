public class Patient {
    private String id;
    private String name;
    private String disease;
    private String sex;
    private String admitStatus;
    private int age;

    public Patient(String id, String name, String disease, String sex, String admitStatus, int age) {
        this.id = id;
        this.name = name;
        this.disease = disease;
        this.sex = sex;
        this.admitStatus = admitStatus;
        this.age = age;
    }

    public static Patient newPatient(java.util.Scanner sc) {
        System.out.println("---- Add New Patient ----");
        String id    = InputUtil.readNonEmptyString(sc, "ID: ");
        String name  = InputUtil.readNonEmptyString(sc, "Name: ");
        String dis   = InputUtil.readNonEmptyString(sc, "Disease: ");
        String sex   = InputUtil.readNonEmptyString(sc, "Sex: ");
        String admit = InputUtil.readNonEmptyString(sc, "Admit Status: ");
        int age      = InputUtil.readInt(sc, "Age: ");
        return new Patient(id, name, dis, sex, admit, age);
    }

    public void showPatientInfo() {
        System.out.printf("%-6s %-15s %-12s %-4s %-12s %-3d%n",
                id, name, disease, sex, admitStatus, age);
    }

    public String toString() {
        return String.format("%-6s %-15s %-12s %-4s %-12s %-3d",
                id, name, disease, sex, admitStatus, age);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisease() { return disease; }
    public String getSex() { return sex; }
    public String getAdmitStatus() { return admitStatus; }
    public int getAge() { return age; }
}
