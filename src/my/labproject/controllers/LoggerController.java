package my.labproject.controllers;

public class LoggerController {

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";

    public LoggerController(){}

    public void INFO(String message){
        if( message != null && !"".equals(message) )
            System.out.println("["+ANSI_BLUE+"INFO"+ANSI_RESET+"] "+message);
    }

    public void WARN(String message){
        if( message != null && !"".equals(message) )
            System.out.println("["+ANSI_YELLOW+"WARN"+ANSI_RESET+"] "+message);
    }

    public void ERROR(String message){
        if( message != null && !"".equals(message) )
            System.out.println("["+ANSI_RED+"ERROR"+ANSI_RESET+"] "+message);
    }

    public void DEBUG(String message){
        if( message != null && !"".equals(message) )
            System.out.println("["+ANSI_GREEN+"DEBUG"+ANSI_RESET+"] "+message);
    }

}
