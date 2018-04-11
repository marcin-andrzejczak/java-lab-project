package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.utils.Statements;

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

    public boolean initialize(){
        Boolean result = fileControl.create(workspace, "dr");

        if(result){
            log.INFO("Successfully created workspace \""+workspace+"\"");
        } else {
            log.ERROR("Could not create workspace \""+workspace+"\"!");
        }

        return result;
    }

    public void interpret(String query){
        String[] components = query.split(" ");

        if( components[0].toUpperCase().equals("CREATE") ) {
            log.DEBUG("Trying to create \"" + components[1] + "\" database.");
            Statements.Create.database(components[1]);
        } else if( components[0].toUpperCase().equals("USE") ) {
            log.DEBUG("Trying to use \"" + components[1] + "\" database.");
            Statements.Use(components[1]);
        } else if( components[0].toUpperCase().equals("USING") ) {
            log.DEBUG("Currently using: \""+config.getUsedWorkspace()+"\" workspace");
            log.DEBUG("Currently using: \""+config.getUsedDatabase()+"\" database");
        }
    }

}
