public class Doctor {
	
    private String id;
    private String name;
    private String specialist;
    private String workTime;
    private String qualification;
    private int room;

    public Doctor(String id, String name, String specialist, String workTime, String qualification, int room) {
        this.id = id;
        this.name = name;
        this.specialist = specialist;
        this.workTime = workTime;
        this.qualification = qualification;
        this.room = room;
    }
   
    public static Doctor createFromInput(java.util.Scanner sc) {
        System.out.println("---- Add New Doctor ----");
        String id            = InputUtil.readNonEmptyString(sc, "ID: ");
        String name          = InputUtil.readNonEmptyString(sc, "Name: ");
        String specialist    = InputUtil.readNonEmptyString(sc, "Specialist: ");
        String workTime      = InputUtil.readNonEmptyString(sc, "Work Time: ");
        String qualification = InputUtil.readNonEmptyString(sc, "Qualification: ");
        int room             = InputUtil.readInt(sc, "Room No: ");
        return new Doctor(id, name, specialist, workTime, qualification, room);
    }

    public static Doctor newDoctor(java.util.Scanner sc) {
        return createFromInput(sc);
    }

    public void showDoctorInfo() {
        System.out.printf("%-6s %-15s %-15s %-12s %-12s Room:%-4d%n",
                id, name, specialist, workTime, qualification, room);
    }


    public String toString() {
        return String.format("%-6s %-15s %-15s %-12s %-12s Room:%-4d",
                id, name, specialist, workTime, qualification, room);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialist() { return specialist; }
    public String getWorkTime() { return workTime; }
    public String getQualification() { return qualification; }
    public int getRoom() { return room; }
}
