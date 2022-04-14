package grn.error;

public class ConsoleHandler {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void handleInfo (String msg) {
        System.out.println(ANSI_GREEN + "[INFO] " + msg + ANSI_RESET);
    }

    public static void handleWarning (String msg) {
        System.out.println(ANSI_YELLOW + "[WARNING] " + msg + ANSI_RESET);
    }

    public static void handleException (Exception e) {
        System.out.println(ANSI_RED + "[ERROR] " + e.getMessage() + ANSI_RESET);
        e.printStackTrace();
    }

}
