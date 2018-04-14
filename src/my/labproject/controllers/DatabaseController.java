package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.utils.Statements;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;

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

        // TODO Add checking which function to execute by comparing command with special regex.
        //      Can be done after implementing all the necessary basic functionality.

        String s = components[0].toUpperCase();
        if ("CREATE".equals(s)) switch (components[1].toUpperCase()) {
            case "DATABASE":
                log.DEBUG("Trying to create \"" + components[2] + "\" database.");
                Statements.Create.database(components[2]);
                break;

            case "TABLE":
                log.DEBUG("Trying to create \"" + components[2] + "\" table inside \"" + config.getUsedDatabase() + "\" database.");
                ArrayList<String> params = new ArrayList<String>(Arrays.asList(query.split("(\\(|\\))")[1].split(",")));
                Statements.Create.table(components[2], params);
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

            case "TABLE":
                log.DEBUG("Trying to show all fields in the table \""+components[2]+"\'.");
                Statements.Show.table(components[2]);
                break;

        } else if ("USE".equals(s)) {
            log.DEBUG("Trying to use \"" + components[1] + "\" database.");
            Statements.use(components[1]);

        } else if ("USING".equals(s)) {
            log.DEBUG("Trying to invoke \"USING\" statement.");
            Statements.using();

        } else if ( "INSERT".equals(s) ) {
            log.DEBUG("Trying to insert data into table");
            ArrayList<String> headers = new ArrayList<String>(Arrays.asList(query.split("([()])")[1].split(",")));
            ArrayList<String> data = new ArrayList<String>(Arrays.asList(query.split("([()])")[3].split(",")));
            Statements.insert(components[2], headers, data);

        } else if ( "SELECT".equals(s) ) {
            log.DEBUG("Trying to select data from table");

            String tableName = query.toUpperCase().split("(FROM)")[1].trim();
            ArrayList<String> headers = new ArrayList<String>(Arrays.asList(query.toUpperCase().split("(SELECT|FROM)")[1].split(",")));
            Statements.select( tableName, headers );

            // SELECT id, name, tax FROM tabelka

        } else if ("EXIT".equals(s)){
            return;

        } else {
            log.ERROR("Syntax error! Command:\n" + components[0] + "\nCould not be interpreted!");
        }

    }

}
