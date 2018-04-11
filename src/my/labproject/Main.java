package my.labproject;

import my.labproject.application.DatabaseApplication;
import my.labproject.controllers.LoggerController;

public class Main {

    private final static LoggerController log = Config.Constants.LOGGER;

    public static void main(String[] args) {
        DatabaseApplication app = new DatabaseApplication();

        log.INFO("Starting application");
	    app.run();
	    log.INFO("Exiting application");
    }
}

