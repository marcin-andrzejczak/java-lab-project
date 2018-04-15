package my.labproject.application;

import my.labproject.Config;
import my.labproject.controllers.CliController;
import my.labproject.controllers.DatabaseController;
import my.labproject.controllers.LoggerController;

public class DatabaseApplication {

    private final CliController cli = new CliController();
    private final DatabaseController db = new DatabaseController();
    private final LoggerController log = Config.Constants.LOGGER;

    public void run(){
        String command;
        boolean answer = true;

        if(db.workspaceExists())
            answer = cli.askYesNo("Workspace already exists! Create new one? [yes/no]:");

        if( answer && !db.initialize()){
            log.ERROR("Error occurred during initialization. Exiting.");
            return;
        }

        log.INFO("Everything started successfully. You may start working now.");
        do{
            command = cli.readCommand();
            if( !"".equals(command))
                db.interpret(command);
        } while ( !command.toUpperCase().equals("EXIT") );

    }

}
