package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.Config.Constants;
import my.labproject.utils.PatternMatcher;
import my.labproject.utils.Statements;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseController {

    private final Config config = new Config();
    private final LoggerController log = new LoggerController();
    private final FileController fileControl = new FileController();
    private final PatternMatcher patternMatcher = new PatternMatcher();
    private final Statements statements = new Statements();
    private String workspace;


    public DatabaseController(){
        this(Constants.DEFAULT_WORKSPACE);
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
        // TODO Add checking which function to execute by comparing command with special regex.
        //      Can be done after implementing all the necessary basic functionality.
        if( !patternMatcher.matchesAnyAvailable(query.toUpperCase()) ){
            log.ERROR("Syntax error! Command could not be interpreted!");
            return;
        } else if( config.getUsedWorkspace() == null ) {
            log.ERROR("Wrong environment settings detected. ");
            log.ERROR("Used workspace: "+config.getUsedWorkspace());
            return;
        }

        String[] components = query.split(" ");
        String s = components[0].toUpperCase();
        if ("CREATE".equals(s)) switch (components[1].toUpperCase()) {
            case "DATABASE":
                log.DEBUG("Trying to create \"" + components[2] + "\" database");
                statements.createDatabase(query);
                break;

            case "TABLE":
                log.DEBUG("Trying to create \"" + components[2] + "\" database");
                statements.createTable(query);
                break;

        } else if ("SHOW".equals(s)) switch (components[1].toUpperCase()) {
            case "DATABASES":
                log.DEBUG("Trying to show all databases.");
                statements.showDatabases();
                break;

            case "TABLES":
                log.DEBUG("Trying to show all tables in database \""+config.getUsedDatabase()+"\'.");
                statements.showTables();
                break;

            case "TABLE":
                log.DEBUG("Trying to show all fields in the table \""+components[2]+"\'.");
                statements.showTable(query);
                break;

        } else if ("USE".equals(s)) {
            log.DEBUG("Trying to use \"" + components[1] + "\" database.");
            statements.use(query);

        } else if ("USING".equals(s)) {
            log.DEBUG("Trying to invoke \"USING\" statement.");
            statements.using();

        } else if ( "INSERT".equals(s) ) {
            log.DEBUG("Trying to insert data into table");
            ArrayList<String> headers = new ArrayList<String>(Arrays.asList(query.split("([()])")[1].split(",")));
            ArrayList<String> data = new ArrayList<String>(Arrays.asList(query.split("([()])")[3].split(",")));
            statements.insert(components[2], headers, data);

        } else if ( "SELECT".equals(s) ) {
            log.DEBUG("Trying to select data from table");
//            String tableName = query.toUpperCase().split("(FROM|WHERE)")[1].trim();
//            ArrayList<String> headers = new ArrayList<String>(Arrays.asList(query.toUpperCase().split("(SELECT|FROM)")[1].split(",")));
//            statements.select( tableName, headers, query);
            statements.selectRf(query);

        } else if ("UPDATE".equals(s)){
            log.DEBUG("Trying to update data in the table");
            log.WARN("Functionality still under construction");
            statements.update(query);

        } else if ("DELETE".equals(s)){
            log.DEBUG("Under construction!");
            statements.delete(query);
        } else if (!"EXIT".equals(s)) {
            log.ERROR("Syntax error! Command could not be interpreted!");
        }

    }

}
