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

        public static final String DEFAULT_WORKSPACE = "DB_TEST/";

        public static final String PROMPT =  "\t-> ";

        public static final int CLI_TRIES_NUMBER = 3;

        public static final String DATA_DELIMITER = ";";

        public static final String SELECT_DATA_SEPARATOR = " | ";

    }

    public static class StatementPatterns {

    // REFACTORED PATTERNS
        public static final Pattern CreateDatabase  = Pattern.compile("^CREATE DATABASE (\\w+)$", Pattern.CASE_INSENSITIVE);

        public static final Pattern CreateTable     = Pattern.compile("^CREATE TABLE (\\w+) ?\\( ?\\w+( ?, ?\\w+)*\\)$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowDatabases   = Pattern.compile("^SHOW DATABASES$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowTables      = Pattern.compile("^SHOW TABLES$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowTable       = Pattern.compile("^SHOW TABLE (\\w+)$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Use             = Pattern.compile("^USE (\\w+)$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Using           = Pattern.compile("^USING$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Select          = Pattern.compile("^SELECT (\\*|\\s?\\w+( ?, ?\\w+)*) FROM (\\w+)( WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+)?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Insert          = Pattern.compile("^INSERT INTO \\w+( ?\\( ?\\w+( ?, ?\\w+)*\\))? VALUES \\( ?\\w+( ?, ?\\w+ ?)*\\)$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Update          = Pattern.compile("^UPDATE \\w+ SET \\w+ ?= ?\\w+( ?, ?\\w+ ?= ?\\w+)* WHERE \\w+ ?==?\\w+$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Delete          = Pattern.compile("^DELETE FROM \\w+ WHERE \\w+ ?==?\\w+$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Exit            = Pattern.compile("^EXIT$", Pattern.CASE_INSENSITIVE);

        // OLD PATTERNS
        @Deprecated
        public static final Pattern Create          = Pattern.compile("^CREATE (DATABASE \\w+|TABLE \\w+ ?\\( ?\\w+( ?, ?\\w+)*\\))$", Pattern.CASE_INSENSITIVE);
        @Deprecated
        public static final Pattern Show            = Pattern.compile("^SHOW (DATABASES|TABLES|TABLE \\w+)$", Pattern.CASE_INSENSITIVE);




    }

}
