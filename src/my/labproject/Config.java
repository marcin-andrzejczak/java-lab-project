package my.labproject;

import java.util.regex.Pattern;

public class Config {

    private static String usedWorkspace;

    private static String usedDatabase;

    public String getUsedWorkspace() {
        return usedWorkspace;
    }

    public void setUsedWorkspace(String workspace) {
        usedWorkspace = workspace;
    }

    public String getUsedDatabase() {
        return usedDatabase;
    }

    public void setUsedDatabase(String database) {
        usedDatabase = database;
    }

    /**
     *  CONSTANTS
     */

    public static class Constants {

        public static final String DEFAULT_WORKSPACE = "E:/DB_TEST/labApplication/";

        public static final String PROMPT =  "\t-> ";

        public static final int CLI_TRIES_NUMBER = 3;

        public static final String DATA_DELIMITER = ";";

        public static final String SELECT_DATA_SEPARATOR = " | ";

    }

    public static class StatementPatterns {

        public static final Pattern Create          = Pattern.compile("^CREATE (DATABASE \\w+|TABLE \\w+ ?\\( ?\\w+( ?, ?\\w+)*\\));$");

        public static final Pattern Show            = Pattern.compile("^SHOW (DATABASES|TABLES|TABLE \\w+);$");

        public static final Pattern Use             = Pattern.compile("^USE \\w+;$");

        public static final Pattern Using           = Pattern.compile("^USING;$");

        public static final Pattern Select          = Pattern.compile("^SELECT (\\*|\\s?\\w+( ?, ?\\w+)*) FROM \\w+( WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+)?;$");

        public static final Pattern Insert          = Pattern.compile("^INSERT INTO \\w+( ?\\( ?\\w+( ?, ?\\w+)*\\))? VALUES \\( ?\\w+( ?, ?\\w+ ?)*\\);$");

        public static final Pattern Update          = Pattern.compile("^UPDATE \\w+ SET \\w+ ?= ?\\w+( ?, ?\\w+ ?= ?\\w+)* WHERE \\w+ ?==?\\w+;$");

        public static final Pattern Delete          = Pattern.compile("^DELETE FROM \\w+ WHERE \\w+ ?==?\\w+;$");

        public static final Pattern Exit            = Pattern.compile("^EXIT;?$");

    }

}
