package my.labproject.controllers;

public class LoggerController {

    private static int severity = Constants.NOTSET;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";

    public LoggerController(){
        this( severity != Constants.NOTSET ? Constants.DISABLED : severity );
    }

    public LoggerController(int severity){
        LoggerController.severity = severity;
    }

    public static int getSeverity() {
        return severity;
    }

    public static void setSeverity(int severity) {
        LoggerController.severity = severity;
    }

    public void INFO(String message){
        if(severity >= Constants.INFO)
            System.out.println("["+ANSI_BLUE+"INFO"+ANSI_RESET+"] "+message);
    }

    public void WARN(String message){
        if(severity >= Constants.WARN)
            System.out.println("["+ANSI_YELLOW+"WARN"+ANSI_RESET+"] "+message);
    }

    public void ERROR(String message){
        if(severity >= Constants.ERROR)
            System.out.println("["+ANSI_RED+"ERROR"+ANSI_RESET+"] "+message);
    }

    public void DEBUG(String message){
        if(severity >= Constants.DEBUG)
            System.out.println("["+ANSI_GREEN+"DEBUG"+ANSI_RESET+"] "+message);
    }

    public static class Constants{
        static final int NOTSET = -1;
        public static final int DISABLED = 0;
        public static final int INFO = 1;
        public static final int WARN = 2;
        public static final int ERROR = 3;
        public static final int DEBUG = 4;
    }

}
