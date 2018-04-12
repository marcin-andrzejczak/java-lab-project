package my.labproject.utils;

import my.labproject.Config;
import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

import java.util.ArrayList;

/**
 *  Statements implementation
 */

public class Statements {

    private static final Config config = Config.Constants.CHANGEABLE;
    private static final LoggerController log = Config.Constants.LOGGER;
    private static final FileController fileControl = Config.Constants.FILE_CONTROLLER;

    public static class Create{

        public static boolean database(String name){
            String path = config.getUsedWorkspace()+name;

            log.DEBUG("Creating database \""+name+"\" with path \""+path+"\".");
            if(fileControl.create(path, "d")) {
                log.INFO("Successfully created database \""+name+"\"");
                return true;
            } else {
                log.ERROR("Could not create database \""+name+"\"!");
            }
            return false;
        }

        public static boolean table(String name){
            String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+name;

            log.DEBUG("Creating table \""+name+"\" with path \""+path+"\".");
            if(fileControl.create(path, "f")) {
                log.INFO("Successfully created table \""+name+"\"");
                return true;
            } else {
                log.ERROR("Could not create table \""+name+"\"!");
            }
            return false;
        }

    }

    public static class Show{

        public static boolean databases(){
            String path = config.getUsedWorkspace();
            ArrayList<String> databases = fileControl.listFiles(path);

            if( databases.size() == 0 ){
                log.INFO("No tables in database \""+config.getUsedDatabase()+"\"");
                return true;
            }

            StringBuilder sb = new StringBuilder("Database\n");
            for(String database : databases){
                if(database.equals(config.getUsedDatabase()))
                    sb.append("     * ").append(database).append("\n");
                else
                    sb.append("       ").append(database).append("\n");
            }

            log.INFO(sb.toString());
            return true;
        }

        public static boolean tables(){
            String path = config.getUsedWorkspace()+"/"+config.getUsedDatabase();
            ArrayList<String> tables = fileControl.listFiles(path);

            if( tables.size() == 0 ){
                log.INFO("No tables in database \""+config.getUsedDatabase()+"\"");
                return true;
            }

            StringBuilder sb = new StringBuilder("Table\n");
            for(String table : tables){
                sb.append("       ").append(table).append("\n");
            }

            log.INFO(sb.toString());
            return true;
        }

    }

    public static boolean use(String databaseName){
        if(config.getUsedWorkspace().isEmpty()){
            log.ERROR("Workspace field is empty!");
        } else {

            String pathname = config.getUsedWorkspace() + databaseName;
            if (fileControl.exists(pathname)) {
                config.setUsedDatabase(databaseName);
                log.INFO("Using database \"" + databaseName + "\"");
                return true;

            } else {
                log.ERROR("Could not use database \"" + databaseName + "\". Database does not exists!");
            }

        }
        return false;
    }

    public static boolean using(){
        log.INFO("Currently use: \""+config.getUsedDatabase()+"\" database and \""+config.getUsedWorkspace()+"\" workspace.");
        return true;
    }

}
