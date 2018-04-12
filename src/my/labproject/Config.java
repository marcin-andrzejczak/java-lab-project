package my.labproject;

import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

public class Config {

    private static final Config config = new Config();

    /**
     *  CONSTANTS
     */

    public static class Constants {

        public static final FileController FILE_CONTROLLER = new FileController();

        public static final LoggerController LOGGER = new LoggerController(LoggerController.Constants.ERROR);

        public static final String DEFAULT_WORKSPACE = "E:/DB_TEST/labApplication/";

        public static final String PROMPT =  "\t-> ";

        public static final int CLI_TRIES_NUMBER = 3;

        public static final Config CHANGEABLE = config;

    }


    /**
     *  CHANGEABLE
     */

    private static String USED_WORKSPACE;

    private static String USED_DATABASE;

    public String getUsedWorkspace() {
        return USED_WORKSPACE;
    }

    public void setUsedWorkspace(String usedWorkspace) {
        USED_WORKSPACE = usedWorkspace;
    }

    public String getUsedDatabase() {
        return USED_DATABASE;
    }

    public void setUsedDatabase(String usedDatabase) {
        USED_DATABASE = usedDatabase;
    }
}
