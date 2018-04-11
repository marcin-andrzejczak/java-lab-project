package my.labproject.utils;

import my.labproject.Config;
import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

/**
 *  Statements implementation
 */

public class Statements {

    private static final Config config = Config.Constants.CHANGEABLE;
    private static final String workspace = config.getUsedWorkspace();
    private static final String database = config.getUsedDatabase();
    private static final LoggerController log = Config.Constants.LOGGER;
    private static final FileController fileControl = Config.Constants.FILE_CONTROLLER;

    public static class Create{

        public static void database(String name){
            if(fileControl.create(workspace+name, "d")) {
                log.INFO("Successfully created database \""+name+"\"");
            } else {
                log.ERROR("Could not create database \""+name+"\"!");
            }
        }

        public static void table(String name){
            if(fileControl.create(workspace+name, "f")) {
                log.INFO("Successfully created database \""+name+"\"");
            } else {
                log.ERROR("Could not create database \""+name+"\"!");
            }
        }

    }

    public static boolean Use(String databaseName){
        if(config.getUsedWorkspace().isEmpty()){
            log.ERROR("Workspace field is empty!");
        }
        String pathname = config.getUsedWorkspace()+databaseName;
        if( fileControl.exists(pathname) ){
            config.setUsedDatabase(databaseName);
            log.INFO("Using database \""+databaseName+"\"");
            return true;
        } else {
            log.ERROR("Could not use database \""+databaseName+"\". Database does not exists!");
        }

        return false;
    }

}
