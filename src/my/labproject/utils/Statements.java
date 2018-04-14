package my.labproject.utils;

import my.labproject.Config;
import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

        public static boolean table(String name, ArrayList<String> params){
            String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+name+".txt";

            log.DEBUG("Creating table \""+name+"\" with path \""+path+"\".");
            if(fileControl.create(path, "f")
                && fileControl.saveLineToFile(path, params)) {
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
                log.INFO("No databases in workspace \""+config.getUsedWorkspace()+"\"");
                return true;
            }

            StringBuilder sb = new StringBuilder("Databases:\n");
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

        public static boolean table(String tableName){
            String path = config.getUsedWorkspace()+"/"+config.getUsedDatabase()+"/"+tableName+".txt";
            ArrayList<String> fields =  fileControl.readHeader(path);

            if( fields.size() == 0 ){
                log.INFO("No fields in table \""+tableName+"\"");
                return true;
            }

            StringBuilder sb = new StringBuilder("Fields:\n");
            for(String database : fields){
                sb.append("       ").append(database).append("\n");
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

    public static boolean select(String tableName, ArrayList<String> headersToGet){
        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";
        ArrayList<String> tableHeaders = fileControl.readHeader(path);
        ArrayList<String> tempTableHeaders = new ArrayList<String>(tableHeaders);
        StringBuilder sb = new StringBuilder();

        if(headersToGet.size() == 1 && headersToGet.get(0).trim().equals("*")) {
            log.DEBUG("Selecting ALL the fields");
            headersToGet = tableHeaders;
        } else {
            log.DEBUG("Selecting only chosen the fields");
            for( String header : tempTableHeaders){
                tempTableHeaders.set(tempTableHeaders.indexOf(header), header.toUpperCase());
            }

            for( String header : headersToGet ){
                if( tempTableHeaders.contains(header.trim().toUpperCase()) ){
                    headersToGet.set(headersToGet.indexOf(header), tableHeaders.get(tempTableHeaders.indexOf(header.trim().toUpperCase())));
                    log.DEBUG("Setting "+ tableHeaders.get(tempTableHeaders.indexOf(header.trim().toUpperCase())));
                }
            }
        }

        for( String header : headersToGet){
            sb.append(header).append(Config.Constants.SELECT_DATA_SEPARATOR);
        }
        sb.delete(sb.lastIndexOf(Config.Constants.SELECT_DATA_SEPARATOR), sb.length()).append("\n       ");

        ArrayList<HashMap<String, String>> data = fileControl.readData(path, headersToGet);
        for( HashMap fields : data ){
            for( String header : headersToGet ){
                sb.append(fields.get(header)).append(Config.Constants.SELECT_DATA_SEPARATOR);
            }
            sb.delete(sb.lastIndexOf(Config.Constants.SELECT_DATA_SEPARATOR), sb.length()).append("\n       ");
        }

        log.INFO(sb.toString());
        return true;
    }

    public static boolean insert(String tableName, ArrayList<String> headers, ArrayList<String> data){
        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";

        if( !fileControl.exists(path)){
            log.ERROR("Could not insert data into table \""+tableName+"\". Table does not exist!");
            return false;
        } else if( headers.size() != data.size() ){
            log.ERROR("Headers and data parameter lists sizes are different!");
            return false;
        }

        HashMap<String, String> dataSet = new HashMap<>();
        for ( String header : headers ){
            dataSet.put( header.trim(), data.get(headers.indexOf(header)).trim() );
        }

        log.DEBUG("Inserting data into table \""+tableName+"\".");
        if(fileControl.insertDataWithHeaders(path, dataSet)) {
            log.INFO("Successfully inserted data into table \""+tableName+"\"");
        } else {
            log.ERROR("Could not insert data into table \""+tableName+"\"!");
            return false;
        }
        return true;
    }

    public static boolean update(){

        return true;
    }

    public static boolean delete(){

        return true;
    }

}
