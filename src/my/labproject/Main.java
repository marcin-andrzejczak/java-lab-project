package my.labproject;

import my.labproject.database.DatabaseCliController;
import my.labproject.logger.Logger;

public class Main {

    public static void main(String[] args) {
	    DatabaseCliController dbCli = new DatabaseCliController();
        Logger log = new Logger();

        log.INFO("Starting application");
	    dbCli.run();
	    log.INFO("Exiting application");
    }
}

