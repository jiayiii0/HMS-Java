import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HospitalManagement {
    //“This program is a console-based Hospital Management System. It starts with some preloaded data, 
	//shows a main menu, and allows us to manage doctors, patients, staff, medicines, labs, and facilities with basic operations like view, add, edit, and delete.”
    private static Doctor[] doctors = new Doctor[25];
    private static Patient[] patients = new Patient[100];
    private static Staff[] staffs = new Staff[100];
    private static Medicine[] medicines = new Medicine[100];
    private static Lab[] labs = new Lab[20];
    private static Facility[] facilities = new Facility[20];

   
    private static int doctorCount = 0;
    private static int patientCount = 0;
    private static int staffCount = 0;
    private static int medicineCount = 0;
    private static int labCount = 0;
    private static int facilityCount = 0;

    private static final Scanner sc = new Scanner(System.in);
    private static boolean exitRequested = false;
    
    

    public static void main(String[] args) {
        showWelcome();    
        preloadData();     
        System.out.println("Welcome to the HMS  |  " + java.time.LocalDateTime.now());
        int choice;
        do {
        	
        	

            System.out.println("\n===== Hospital Management System =====");
            System.out.println("1. Manage Doctors");
            System.out.println("2. Manage Patients");
            System.out.println("3. Manage Staff");
            System.out.println("4. Manage Medicines");
            System.out.println("5. Manage Labs");
            System.out.println("6. Manage Facilities");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1: doctorMenu(); break;
                case 2: patientMenu(); break;
                case 3: staffMenu(); break;
                case 4: medicineMenu(); break;
                case 5: labMenu(); break;
                case 6: facilityMenu(); break;
                case 0: System.out.println("Exiting system... Goodbye!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0 && !exitRequested);
    }

    
    // ---------- Welcome ----------
    private static void showWelcome() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Welcome to the Hospital Management System");
        System.out.println("Current Date & Time: " + now);
    }

    // ---------- Preload Data ----------
    private static void preloadData() {
      
        doctors[doctorCount++] = new Doctor("806", "SU GUYUN", "Cardiology", "9AM-5PM", "MBBS", 101);
        doctors[doctorCount++] = new Doctor("498", "Chee Jia Bao", "Neurology", "10AM-6PM", "MD", 102);
        doctors[doctorCount++] = new Doctor("D003", "Rahman", "Orthopedics", "11AM-7PM", "MBBS", 103);

        patients[patientCount++] = new Patient("P001", "Ahmad", "Flu", "M", "Admitted", 30);
        patients[patientCount++] = new Patient("P002", "Aisyah", "Dengue", "F", "Admitted", 25);
        patients[patientCount++] = new Patient("P003", "John", "Covid", "M", "Discharged", 40);

        staffs[staffCount++] = new Nurse("S001", "Nora", "F", 2500, "Pediatrics");
        staffs[staffCount++] = new Pharmacist("S002", "Azlan", "M", 3000, "Pharma License 123");
        staffs[staffCount++] = new Security("S003", "Ravi", "M", 2000, "Night Shift");

        medicines[medicineCount++] = new Medicine("Paracetamol", "PharmaX", "2026-05", 10, 100);
        medicines[medicineCount++] = new Medicine("Amoxicillin", "MediCorp", "2025-12", 15, 50);
        medicines[medicineCount++] = new Medicine("Ibuprofen", "HealthPlus", "2027-03", 12, 80);

        labs[labCount++] = new Lab("X-Ray", 200);
        labs[labCount++] = new Lab("Blood Test", 100);
        labs[labCount++] = new Lab("MRI", 500);

        facilities[facilityCount++] = new Facility("ICU");
        facilities[facilityCount++] = new Facility("Emergency Room");
        facilities[facilityCount++] = new Facility("General Ward");
    }

    // ---------- Doctors ----------
    private static void doctorMenu() {
        int choice;
        do {
            System.out.println("\n--- Doctor Menu ---");
            System.out.println("1. View Doctors");
            System.out.println("2. Add Doctor");
            System.out.println("3. Edit Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewDoctors();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addDoctor();
                    if (handleNavAfterAction()) return;
                    break;
                case 3:
                    editDoctor();
                    if (handleNavAfterAction()) return;
                    break;
                case 4:
                    deleteDoctor();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewDoctors() {
        System.out.println("\nList of Doctors:");
        for (int i = 0; i < doctorCount; i++) {
            if (doctors[i] != null) {
                doctors[i].showDoctorInfo(); 
            }
        }
    }

    // ---- helpers for Doctor ----
   
    private static String nextDoctorId() {
        int max = 0;
        for (int i = 0; i < doctorCount; i++) {
            Doctor d = doctors[i];
            if (d == null) continue;
            String id = d.getId();
            if (id != null && id.matches("D\\d{3}")) {
                int n = Integer.parseInt(id.substring(1));
                if (n > max) max = n;
            }
        }
        return "D" + String.format("%03d", max + 1);
    }

    private static int findDoctorIndexById(String id) {
        for (int i = 0; i < doctorCount; i++) {
            if (doctors[i] != null && doctors[i].getId().equals(id)) return i;
        }
        return -1;
    }

    private static void addDoctor() {
        if (doctorCount >= doctors.length) {
            System.out.println("Doctor list full!");
            return;
        }
        String id = nextDoctorId(); 
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Specialist: ");
        String spec = sc.nextLine().trim();
        System.out.print("Enter Work Time: ");
        String time = sc.nextLine().trim();
        System.out.print("Enter Qualification: ");
        String qual = sc.nextLine().trim();
        System.out.print("Enter Room: ");
        int room = safeIntInput();

        doctors[doctorCount++] = new Doctor(id, name, spec, time, qual, room);
        System.out.println("Doctor added successfully with ID: " + id);
    }

    private static void editDoctor() {
        System.out.print("Enter Doctor ID to edit: ");
        String id = sc.nextLine().trim();
        int idx = findDoctorIndexById(id);
        if (idx == -1) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        Doctor d = doctors[idx];
        d.showDoctorInfo();

       
        System.out.print("New Name (blank=keep '" + d.getName() + "'): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = d.getName();

        System.out.print("New Specialist (blank=keep '" + d.getSpecialist() + "'): ");
        String spec = sc.nextLine().trim();
        if (spec.isEmpty()) spec = d.getSpecialist();

        System.out.print("New Work Time (blank=keep '" + d.getWorkTime() + "'): ");
        String time = sc.nextLine().trim();
        if (time.isEmpty()) time = d.getWorkTime();

        System.out.print("New Qualification (blank=keep '" + d.getQualification() + "'): ");
        String qual = sc.nextLine().trim();
        if (qual.isEmpty()) qual = d.getQualification();

        System.out.print("New Room (blank=keep '" + d.getRoom() + "'): ");
        String roomStr = sc.nextLine().trim();
        int room = d.getRoom();
        if (!roomStr.isEmpty()) {
            try {
                room = Integer.parseInt(roomStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Keeping old room " + room + ".");
            }
        }

        doctors[idx] = new Doctor(d.getId(), name, spec, time, qual, room);
        System.out.println("Doctor updated successfully!");
    }

    private static void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        String id = sc.nextLine().trim();
        int index = findDoctorIndexById(id);
        if (index == -1) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        for (int i = index; i < doctorCount - 1; i++) {
            doctors[i] = doctors[i + 1];
        }
        doctors[doctorCount - 1] = null;
        doctorCount--;
        System.out.println("Doctor with ID " + id + " deleted successfully!");
    }

    // ---------- Patients ----------
    private static void patientMenu() {
        int choice;
        do {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Patients");
            System.out.println("2. Add Patient");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewPatients();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addPatient();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewPatients() {
        System.out.println("\nList of Patients:");
        for (int i = 0; i < patientCount; i++) {
            if (patients[i] != null) {
                System.out.println(patients[i]);
            }
        }
    }

    private static void addPatient() {
        if (patientCount >= patients.length) {
            System.out.println("Patient list full!");
            return;
        }
        System.out.print("Enter ID: "); String id = sc.nextLine().trim();
        System.out.print("Enter Name: "); String name = sc.nextLine().trim();
        System.out.print("Enter Disease: "); String dis = sc.nextLine().trim();
        System.out.print("Enter Sex: "); String sex = sc.nextLine().trim();
        System.out.print("Enter Admit Status: "); String status = sc.nextLine().trim();
        System.out.print("Enter Age: "); int age = safeIntInput();

        patients[patientCount++] = new Patient(id, name, dis, sex, status, age);
        System.out.println("Patient added successfully!");
    }

    // ---------- Staff ----------
    private static void staffMenu() {
        int choice;
        do {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("1. View Staff");
            System.out.println("2. Add Staff");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewStaff();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addStaff();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewStaff() {
        System.out.println("\nList of Staff:");
        for (int i = 0; i < staffCount; i++) {
            if (staffs[i] != null) {
                System.out.println(staffs[i]);
            }
        }
    }

    private static void addStaff() {
        if (staffCount >= staffs.length) {
            System.out.println("Staff list full!");
            return;
        }
        System.out.print("Enter ID: "); String id = sc.nextLine().trim();
        System.out.print("Enter Name: "); String name = sc.nextLine().trim();
        System.out.print("Enter Sex: "); String sex = sc.nextLine().trim();
        System.out.print("Enter Salary: "); int salary = safeIntInput();
        System.out.print("Enter Role (Nurse/Pharmacist/Security): "); String role = sc.nextLine().trim();
        System.out.print("Enter Extra Info (Dept/License/Shift): "); String extra = sc.nextLine().trim();

        Staff s;
        if (role.equalsIgnoreCase("Nurse")) {
            s = new Nurse(id, name, sex, salary, extra);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            s = new Pharmacist(id, name, sex, salary, extra);
        } else {
            s = new Security(id, name, sex, salary, extra);
        }
        staffs[staffCount++] = s;
        System.out.println("Staff added successfully!");
    }

    // ---------- Medicines ----------
    private static void medicineMenu() {
        int choice;
        do {
            System.out.println("\n--- Medicine Menu ---");
            System.out.println("1. View Medicines");
            System.out.println("2. Add Medicine");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewMedicines();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addMedicine();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewMedicines() {
        System.out.println("\nList of Medicines:");
        for (int i = 0; i < medicineCount; i++) {
            if (medicines[i] != null) {
                System.out.println(medicines[i]);
            }
        }
    }

    private static void addMedicine() {
        if (medicineCount >= medicines.length) {
            System.out.println("Medicine list full!");
            return;
        }
        System.out.print("Enter Name: "); String name = sc.nextLine().trim();
        System.out.print("Enter Manufacturer: "); String manu = sc.nextLine().trim();
        System.out.print("Enter Expiry: "); String exp = sc.nextLine().trim();
        System.out.print("Enter Cost: "); int cost = safeIntInput();
        System.out.print("Enter Count: "); int count = safeIntInput();

        medicines[medicineCount++] = new Medicine(name, manu, exp, cost, count);
        System.out.println("Medicine added successfully!");
    }

    // ---------- Labs ----------
    private static void labMenu() {
        int choice;
        do {
            System.out.println("\n--- Lab Menu ---");
            System.out.println("1. View Labs");
            System.out.println("2. Add Lab");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewLabs();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addLab();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewLabs() {
        System.out.println("\nList of Labs:");
        for (int i = 0; i < labCount; i++) {
            if (labs[i] != null) {
                System.out.println(labs[i]);
            }
        }
    }

    private static void addLab() {
        if (labCount >= labs.length) {
            System.out.println("Lab list full!");
            return;
        }
        System.out.print("Enter Lab Name: "); String name = sc.nextLine().trim();
        System.out.print("Enter Cost: "); int cost = safeIntInput();

        labs[labCount++] = new Lab(name, cost);
        System.out.println("Lab added successfully!");
    }

    // ---------- Facilities ----------
    private static void facilityMenu() {
        int choice;
        do {
            System.out.println("\n--- Facility Menu ---");
            System.out.println("1. View Facilities");
            System.out.println("2. Add Facility");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = safeIntInput();

            switch (choice) {
                case 1:
                    viewFacilities();
                    if (handleNavAfterAction()) return;
                    break;
                case 2:
                    addFacility();
                    if (handleNavAfterAction()) return;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exitRequested);
    }

    private static void viewFacilities() {
        System.out.println("\nList of Facilities:");
        for (int i = 0; i < facilityCount; i++) {
            if (facilities[i] != null) {
                System.out.println(facilities[i]);
            }
        }
    }

    private static void addFacility() {
        if (facilityCount >= facilities.length) {
            System.out.println("Facility list full!");
            return;
        }
        System.out.print("Enter Facility: "); String fac = sc.nextLine().trim();

        facilities[facilityCount++] = new Facility(fac);
        System.out.println("Facility added successfully!");
    }

 
    private static boolean handleNavAfterAction() {
        while (true) {
            System.out.println("\nWhat next?");
            System.out.println("1. Return to previous selection (continue here)");
            System.out.println("2. Return to Main Menu");
            System.out.println("0. Exit Program");
            System.out.print("Choice: ");
            int nav = safeIntInput();
            if (nav == 1) {
                return false; 
            } else if (nav == 2) {
                return true;  
            } else if (nav == 0) {
                exitRequested = true;
                return true; 
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ---------- Safe Input ----------
    private static int safeIntInput() {
        while (true) {
            try {
                String input = sc.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }
    
    
}
