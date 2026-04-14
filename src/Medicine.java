public class Medicine {
    private String name;
    private String manufacturer;
    private String expiryDate;
    private int cost;
    private int count;

    public Medicine(String name, String manufacturer, String expiryDate, int cost, int count) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.cost = cost;
        this.count = count;
    }

  
    public static Medicine newMedicine(java.util.Scanner sc) {
        System.out.println("---- Add New Medicine ----");
        String name = InputUtil.readNonEmptyString(sc, "Name: ");
        String manu = InputUtil.readNonEmptyString(sc, "Manufacturer: ");
        String exp  = InputUtil.readNonEmptyString(sc, "Expiry (e.g. 2026-05): ");
        int cost    = InputUtil.readInt(sc, "Cost: ");
        int count   = InputUtil.readInt(sc, "Count: ");
        return new Medicine(name, manu, exp, cost, count);
    }


    public void showMedicine() {
        System.out.printf("%-15s %-14s Exp:%-7s Cost:%-5d Qty:%-4d%n",
                name, manufacturer, expiryDate, cost, count);
    }

    public void findMedicine() {
        showMedicine();
    }

    public String toString() {
        return String.format("%-15s %-14s Exp:%-7s Cost:%-5d Qty:%-4d",
                name, manufacturer, expiryDate, cost, count);
    }

    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public String getExpiryDate() { return expiryDate; }
    public int getCost() { return cost; }
    public int getCount() { return count; }
}
