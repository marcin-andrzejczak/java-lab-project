package my.labproject;

import my.labproject.application.DatabaseApplication;
import my.labproject.controllers.LoggerController;

import java.util.Arrays;

public class Main {

    private final static LoggerController log = new LoggerController();

    public static void main(String[] args) {
        DatabaseApplication app = new DatabaseApplication();

        log.INFO("Starting application");
        try {
            app.run();
        } catch (Exception ex){
            log.ERROR("Exception occurred!\n");
            ex.printStackTrace();
        }
	    log.INFO("Exiting application");
    }
}

