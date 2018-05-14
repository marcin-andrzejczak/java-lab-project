package my.labproject;

import java.util.regex.Pattern;

public class Config {

    private static String usedWorkspace = null;

    private static String usedDatabase = null;

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

        public static final Pattern CreateDatabase  = Pattern.compile("^CREATE DATABASE (\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern CreateTable     = Pattern.compile("^CREATE TABLE (\\w+) ?\\( ?(\\w+( ?, ?\\w+)*)\\);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowDatabases   = Pattern.compile("^SHOW DATABASES;?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowTables      = Pattern.compile("^SHOW TABLES;?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern ShowTable       = Pattern.compile("^SHOW TABLE (\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Use             = Pattern.compile("^USE (\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Using           = Pattern.compile("^USING;?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Select          = Pattern.compile("^SELECT (\\*|\\s?\\w+( ?, ?\\w+)*) FROM (\\w+)( WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+)?;?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Insert          = Pattern.compile("^INSERT INTO (\\w+) ?\\(( ?\\w+( ?, ?\\w+)*)?\\) VALUES ?\\( ?(.+( ?, ?.+ ?)*)\\);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Update          = Pattern.compile("^UPDATE (\\w+) SET (\\w+ ?= ?\\w+( ?, ?\\w+ ?= ?\\w+)*) WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+;?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Delete          = Pattern.compile("^DELETE FROM (\\w+) WHERE (\\w+ ?(>|>=|==|<=|<|!=) ?\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern DropDatabase    = Pattern.compile("^DROP DATABASE (\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern DropTable       = Pattern.compile("^DROP TABLE (\\w+);?$", Pattern.CASE_INSENSITIVE);

        public static final Pattern Exit            = Pattern.compile("^EXIT;?$", Pattern.CASE_INSENSITIVE);

    }

}
