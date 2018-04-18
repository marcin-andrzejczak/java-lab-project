package my.labproject.utils;

import my.labproject.Config;
import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

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
    private static final PatternMatcher patternMatcher = Config.Constants.PATTERN_MATCHER;

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
            if( config.getUsedDatabase() != null && fileControl.create(path, "f")
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

            StringBuilder sb = new StringBuilder("Tables:\n");
            for(String table : tables){
                sb.append("       ").append(table.substring(0, table.lastIndexOf(".txt"))).append("\n");
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

    public static boolean select(String tableName, ArrayList<String> headersToGet, String query){
        // TODO check if table exists
        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";
        if( !fileControl.exists(path) ) {
            log.ERROR("Could not select data from table \""+tableName+"\", because it does not exist!");
            return false;
        }

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

        log.DEBUG(tableName);
        log.DEBUG(headersToGet.toString());
        for( String header : headersToGet){
            sb.append(header).append(Config.Constants.SELECT_DATA_SEPARATOR);
        }
        sb.delete(sb.lastIndexOf(Config.Constants.SELECT_DATA_SEPARATOR), sb.length()).append("\n       ");

        ArrayList<HashMap<String, String>> data;
        log.DEBUG(query);
        if(query.toUpperCase().matches("^SELECT (\\*|\\s?\\w+( ?, ?\\w+)*) FROM \\w+ WHERE \\w+ ?(>|>=|==|<=|<|!=) ?.+;?$")) {
            log.DEBUG("Entering where clause");
            String conditionPart = query.split("WHERE|where")[1];
            String headerWhere = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[0].trim();
            String operator = patternMatcher.retrieve("(>|>=|==|<=|<|!=)", conditionPart);
            String value = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[1].trim();

            data = where(fileControl.readData(path), headerWhere, operator, value);
        } else {
            data = fileControl.readData(path, headersToGet);
        }

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

    // TODO absolutely not working, just selecting with where clause right now ( served a purpose as a playground )
    public static boolean update(String query){
        String tableName = query.split("(update|set)")[1].trim();
        log.WARN(tableName);

        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";
        if( !fileControl.exists(path) ){
            log.ERROR("Table does not exist");
            return false;
        }

        String setPart = query.split("(set|where)")[1];
        ArrayList<String> setList = new ArrayList<String>(Arrays.asList(setPart.trim().split(",")));
        HashMap<String, String> setMap = new HashMap<>();

        for(String set : setList){
            setMap.put(set.split("=")[0].trim(), set.split("=")[1].trim());
        }

        String conditionPart = query.split("WHERE|where")[1];
        String headerWhere = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[0].trim();
        String operator = patternMatcher.retrieve("(>|>=|==|<=|<|!=)", conditionPart);
        String value = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[1].trim();

        if( "".equals(headerWhere) || "".equals(operator) || "".equals(value) )
            return false;

        ArrayList<String> headers = fileControl.readHeader(path);
        ArrayList<HashMap<String, String>> data = fileControl.readData(path);


        ArrayList<HashMap<String, String>> result;
        for(HashMap<String, String> row : data){
            //row.put();
        }

        log.INFO(data.toString());

        return true;
//        String headerWhere = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[0].trim();

//        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";
//
//
//
//        if( !fileControl.exists(path) ){
//            log.ERROR("Table does not exist");
//            return false;
//        }
//
//        ArrayList<String> headers = fileControl.readHeader(config.getUsedWorkspace()+config.getUsedDatabase()+"/hehe.txt");
//        ArrayList<HashMap<String, String>> data = fileControl.readData(config.getUsedWorkspace()+config.getUsedDatabase()+"/hehe.txt");
//
//
//
//        fileControl.insertAllData(config.getUsedWorkspace()+config.getUsedDatabase()+"/test.txt", data, headers);
//
//        return true;
    }



    // TODO deleting lines with where
    public static boolean delete(){

        return true;
    }


    // Helper functions

    private static ArrayList<HashMap<String, String>> where(ArrayList<HashMap<String, String>> allData, String header, String operator, String value){
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for( HashMap<String, String> data : allData){

            if( fulfillsCondition(data.get(header), operator, value) )
                result.add(data);

        }

        return result;
    }

    private static boolean fulfillsCondition(String leftValue, String operator, String rightValue){
        boolean isNumeric;
        Integer number = null;
        try{
            number = Integer.parseInt(leftValue);
            isNumeric = true;
        } catch( Exception ex ){
            isNumeric = false;
        }

        if( (">".equals(operator) || ">=".equals(operator)) && isNumeric ){
            // TODO
        }
        if( ">=".equals(operator) || "==".equals(operator) || "<=".equals(operator) ){
            if( leftValue.equals(rightValue) ) return true;
            else return false;
        }
        if( "<=".equals(operator) || "<".equals(operator) ){
            // TODO
        }
        if( "!=".equals(operator) ){
            if( !leftValue.equals(rightValue) ) return true;
            else return false;
        }

        return false;
    }

}
