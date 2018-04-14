package my.labproject;

import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

import java.util.regex.Pattern;

public class Config {

    private static final Config config = new Config();

    /**
     *  CONSTANTS
     */

    public static class Constants {

        public static final FileController FILE_CONTROLLER = new FileController();

        public static final LoggerController LOGGER = new LoggerController(LoggerController.Constants.DEBUG);

        public static final String DEFAULT_WORKSPACE = "E:/DB_TEST/labApplication/";

        public static final String PROMPT =  "\t-> ";

        public static final int CLI_TRIES_NUMBER = 3;

        public static final Config CHANGEABLE = config;

        public static final String DATA_DELIMITER = ";";

        public static final String SELECT_DATA_SEPARATOR = " | ";

    }

    public static class StatementPatterns {

        // TODO work out the patterns for statements

        public static final Pattern CreateDatabase  = Pattern.compile("");

        public static final Pattern CreateTable     = Pattern.compile("");

        public static final Pattern ShowDatabases   = Pattern.compile("");

        public static final Pattern ShowTables      = Pattern.compile("");

        public static final Pattern ShowTable       = Pattern.compile("");

        public static final Pattern Use             = Pattern.compile("");

        public static final Pattern Using           = Pattern.compile("");

        public static final Pattern Select          = Pattern.compile("");

        public static final Pattern Insert          = Pattern.compile("");

        public static final Pattern Update          = Pattern.compile("");

        public static final Pattern Delete          = Pattern.compile("");


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
