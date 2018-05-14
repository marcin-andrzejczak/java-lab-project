package my.labproject.utils;

import my.labproject.Config;

public class PathUtil {

    private final Config config = new Config();

    public String getTablePath(String tName){
        if( tName == null || config.getUsedDatabase() == null ) return null;
        return getDbPath(config.getUsedDatabase()).concat("/".concat(tName).concat(".txt"));
    }

    public String getDbPath(String dbName){
        if( dbName == null  || config.getUsedWorkspace() == null ) return null;
        return config.getUsedWorkspace().concat(dbName);
    }

}
