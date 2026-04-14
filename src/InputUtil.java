import java.util.Scanner;

// a helper for user input. It makes sure strings aren’t empty, numbers are valid, 
//values can be restricted to a range, and yes/no answers are handled properly.”

public final class InputUtil {
    private InputUtil() {}

    public static String readNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Input cannot be empty. Try again.");
        }
    }


    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }


    public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = readInt(sc, prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Value must be between " + min + " and " + max + ".");
        }
    }


    public static boolean readYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no"))  return false;
            System.out.println("Please enter Y or N.");
        }
    }
}
