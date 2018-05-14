package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.Config.Constants;
import my.labproject.utils.CommandMatcher;

public class DatabaseController {

    private final Config config = new Config();
    private final LoggerController log = new LoggerController();
    private final FileController fileControl = new FileController();
    private final CommandMatcher commandMatcher = new CommandMatcher();
    private final StatementsController statements = new StatementsController();
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
        if( !commandMatcher.matchesAnyAvailable(query, statements) ){
            log.ERROR("Syntax error! Command could not be interpreted!");
        } else if( config.getUsedWorkspace() == null ) {
            log.ERROR("Wrong environment settings detected. ");
            log.ERROR("Used workspace: "+config.getUsedWorkspace());
        }
    }

}
