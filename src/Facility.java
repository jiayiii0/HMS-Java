public class Facility {
	
    private String facility;

    public Facility(String facility) {
        this.facility = facility;
    }

    public static Facility newFacility(java.util.Scanner sc) {
        System.out.println("---- Add New Facility ----");
        String name = InputUtil.readNonEmptyString(sc, "Facility: ");
        return new Facility(name);
    }

    public void showFacility() {
        System.out.printf("%s%n", facility);
    }

    public String toString() {
        return facility;
    }

    public String getFacility() { return facility; }
}
