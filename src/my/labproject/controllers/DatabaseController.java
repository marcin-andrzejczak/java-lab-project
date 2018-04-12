package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.utils.Statements;

import java.beans.Expression;

public class DatabaseController {

    private final Config config = Config.Constants.CHANGEABLE;
    private final LoggerController log = Config.Constants.LOGGER;
    private final FileController fileControl = Config.Constants.FILE_CONTROLLER;
    private String workspace = Config.Constants.DEFAULT_WORKSPACE;


    public DatabaseController(){
        this(Config.Constants.DEFAULT_WORKSPACE);
    }

    public DatabaseController(String workspace){
        config.setUsedWorkspace(workspace);
        this.workspace = workspace;
    }

    public boolean workspaceExists(){
        return fileControl.exists(config.getUsedWorkspace());
    }

    public boolean initialize(){
        if( fileControl.exists(config.getUsedWorkspace()) )
            fileControl.deleteAll(config.getUsedWorkspace());

        Boolean result = fileControl.create(workspace, "dr");

        if(result){
            log.INFO("Successfully created workspace \""+workspace+"\"");
        } else {
            log.ERROR("Could not create workspace \""+workspace+"\"!");
        }

        return result;
    }

    public void interpret(String query) {
        String[] components = query.split(" ");

        String s = components[0].toUpperCase();
        if ("CREATE".equals(s)) switch (components[1].toUpperCase()) {
            case "DATABASE":
                log.DEBUG("Trying to create \"" + components[2] + "\" database.");
                Statements.Create.database(components[2]);
                break;

            case "TABLE":
                log.DEBUG("Trying to create \"" + components[2] + "\" table inside \"" + config.getUsedDatabase() + "\" database.");
                Statements.Create.table(components[2]);
                break;

            default:
                log.ERROR("Syntax error! Usage: CREATE {DATABASE|TABLE} name");
                break;

        } else if ("SHOW".equals(s)) switch (components[1].toUpperCase()) {
            case "DATABASES":
                log.DEBUG("Trying to show all databases.");
                Statements.Show.databases();
                break;

            case "TABLES":
                log.DEBUG("Trying to show all tables in database \""+config.getUsedDatabase()+"\'.");
                Statements.Show.tables();
                break;

        } else if ("USE".equals(s)) {
            log.DEBUG("Trying to use \"" + components[1] + "\" database.");
            Statements.use(components[1]);

        } else if ("USING".equals(s)) {
            log.DEBUG("Trying to invoke \"USING\" statement.");
            Statements.using();

        } else if ("EXIT".equals(s)){
            return;

        } else {
            log.ERROR("Syntax error! Command:\n" + components[0] + "\nCould not be interpreted!");
        }

    }

}
