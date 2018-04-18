package my.labproject;

import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;
import my.labproject.utils.PatternMatcher;

import java.util.regex.Pattern;

public class Config {

    private static final Config config = new Config();

    /**
     *  CONSTANTS
     */

    public static class Constants {

        public static final FileController FILE_CONTROLLER = new FileController();

        public static final PatternMatcher PATTERN_MATCHER = new PatternMatcher();

        public static final LoggerController LOGGER = new LoggerController(LoggerController.Constants.DEBUG);

        public static final String DEFAULT_WORKSPACE = "E:/DB_TEST/labApplication/";

        public static final String PROMPT =  "\t-> ";

        public static final int CLI_TRIES_NUMBER = 3;

        public static final Config CHANGEABLE = config;

        public static final String DATA_DELIMITER = ";";

        public static final String SELECT_DATA_SEPARATOR = " | ";

    }

    // TODO work out the patterns for statements
    public static class StatementPatterns {

        public static final Pattern Create          = Pattern.compile("^CREATE (DATABASE \\w+|TABLE \\w+ ?\\( ?\\w+( ?, ?\\w+)*\\));$");

        public static final Pattern Show            = Pattern.compile("^SHOW (DATABASES|TABLES|TABLE \\w+);$");

        public static final Pattern Use             = Pattern.compile("^USE \\w+;$");

        public static final Pattern Using           = Pattern.compile("^USING;$");

        public static final Pattern Select          = Pattern.compile("^SELECT (\\*|\\s?\\w+( ?, ?\\w+)*) FROM \\w+( WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+)?;$");

        public static final Pattern Insert          = Pattern.compile("^INSERT INTO \\w+( ?\\( ?\\w+( ?, ?\\w+)*\\))? VALUES \\( ?\\w+( ?, ?\\w+ ?)*\\);$");

        // TODO after creating the method for it (just for the sake of easier testing
        public static final Pattern Update          = Pattern.compile("^UPDATE .*$");

//        public static final Pattern Delete          = Pattern.compile("");

        public static final Pattern Exit            = Pattern.compile("^EXIT;?$");
//    UPDATE table_name
//    SET column1 = value1, column2 = value2, ...
//    WHERE condition;

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
