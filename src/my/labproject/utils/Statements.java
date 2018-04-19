package my.labproject.utils;

import my.labproject.Config;
import my.labproject.Config.StatementPatterns;
import my.labproject.controllers.FileController;
import my.labproject.controllers.LoggerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *  Statements implementation
 */

public class Statements {

    private static final Config config = new Config();
    private static final LoggerController log = new LoggerController();
    private static final FileController fileControl = new FileController();
    private static final PatternMatcher patternMatcher = new PatternMatcher();


    // REFACTORED
    public boolean createDatabase(String query){
        // SYNTAX: CREATE DATABASE dbName
        String dbName = patternMatcher.retrieve(StatementPatterns.CreateDatabase, query, 1);

        if( fileControl.create(getDbPath(dbName), "d") ){
            log.INFO("Successfully created database: \""+dbName+"\"");
            return true;
        } else {
            log.ERROR("Could not create database \""+dbName+"\"");
        }
        return false;
    }

    public boolean createTable(String name, ArrayList<String> params){
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


    /*
     *  TODO - better param retriever
     */
    public boolean createTable(String query){
        // SYNTAX: CREATE TABLE (param1, param2)
        String tableName = patternMatcher.retrieve(StatementPatterns.CreateTable, query, 1);
        ArrayList<String> params = new ArrayList<>(Arrays.asList(query.split("([()])")[1].split(",")));
        String path = getTablePath(tableName);

        log.INFO(patternMatcher.getGroupsCount(StatementPatterns.CreateTable, query).toString());

        if( fileControl.create(path, "f") && fileControl.saveLineToFile(path, params)){
            log.INFO("Successfully created table: \""+tableName+"\"");
            return true;
        } else {
            log.ERROR("Could not create table \""+tableName+"\"");
        }
        return false;
    }

    /*
     *  TODO - Current state: mildly refactored
     */
    public boolean showDatabases(){
        ArrayList<String> databases = fileControl.listFiles(config.getUsedWorkspace());

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


    // REFACTORED
    public boolean showTables(){
        ArrayList<String> tables = fileControl.listFiles(getDbPath(config.getUsedDatabase()));

        if( tables.size() == 0 ){
            log.INFO("No tables in database \""+config.getUsedDatabase()+"\"");
            return true;
        }

        StringBuilder sb = new StringBuilder("Tables:\n");
        for(String table : tables){
            sb.append("       ").append(table, 0, table.lastIndexOf(".txt")).append("\n");
        }

        log.INFO(sb.toString());
        return true;
    }

    // REFACTORED
    public boolean showTable(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.ShowTable, query, 1);
        String path = getTablePath(tableName);
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

    // REFACTORED
    public boolean use(String query){
        String dbName = patternMatcher.retrieve(StatementPatterns.Use, query, 1);

        if(config.getUsedWorkspace().isEmpty()){
            log.ERROR("Workspace field is empty!");
        } else {

            String path = getDbPath(dbName);
            if (fileControl.exists(path)) {
                config.setUsedDatabase(dbName);
                log.INFO("Using database \"" + dbName + "\"");
                return true;

            } else {
                log.ERROR("Could not use database \"" + dbName + "\". Database does not exists!");
            }

        }
        return false;
    }

    // NO NEED FOR REFACTORING
    public boolean using(){
        log.INFO("Currently use: \""+config.getUsedDatabase()+"\" database and \""+config.getUsedWorkspace()+"\" workspace.");
        return true;
    }

    // TODO !! REFACTOR !!
    public boolean select(String tableName, ArrayList<String> headersToGet, String query){
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

    // TODO !! REFACTOR !!
    public boolean insert(String tableName, ArrayList<String> headers, ArrayList<String> data){
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

    // TODO !! REFACTOR !!
    public boolean update(String query){
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

        ArrayList<String> headers = fileControl.readHeader(path);
        ArrayList<HashMap<String, String>> data = fileControl.readData(path);

        if( "".equals(headerWhere) || "".equals(operator) || "".equals(value) || headers == null || data == null) {
            return false;
        }

        for( HashMap<String, String> row : data )
            if( row.containsKey(headerWhere) && fulfillsCondition(row.get(headerWhere), operator, value) )
                for (String key : setMap.keySet())
                    row.put(key, setMap.get(key));

        fileControl.insertAllData(path, data, headers);

        return true;
    }

    // TODO !! REFACTOR !!
    public boolean delete(String query){
        String tableName = query.split("(from|where)")[1].trim();
        log.WARN(tableName);

        String path = config.getUsedWorkspace()+config.getUsedDatabase()+"/"+tableName+".txt";
        if( !fileControl.exists(path) ){
            log.ERROR("Table does not exist");
            return false;
        }

        String conditionPart = query.split("WHERE|where")[1];
        String headerWhere = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[0].trim();
        String operator = patternMatcher.retrieve("(>|>=|==|<=|<|!=)", conditionPart);
        String value = conditionPart.trim().split("(>|>=|==|<=|<|!=)")[1].trim();

        ArrayList<String> headers = fileControl.readHeader(path);
        ArrayList<HashMap<String, String>> data = fileControl.readData(path);

        if( "".equals(headerWhere) || "".equals(operator) || "".equals(value) || headers == null || data == null) {
            return false;
        }

        ArrayList<HashMap<String, String>> toDelete = new ArrayList<>();
        for( HashMap<String, String> row : data )
            if( row.containsKey(headerWhere) && fulfillsCondition(row.get(headerWhere), operator, value) )
                toDelete.add(row);
        data.removeAll(toDelete);

        fileControl.insertAllData(path, data, headers);
        return true;
    }


    /*
     *  HELPER FUNCTIONS
     */

    private String getTablePath(String tName){
        return getDbPath(config.getUsedDatabase()).concat("/".concat(tName));
    }

    private String getDbPath(String dbName){
        return config.getUsedWorkspace().concat(dbName);
    }

    private static ArrayList<HashMap<String, String>> where(ArrayList<HashMap<String, String>> allData, String header, String operator, String value){
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        for( HashMap<String, String> data : allData){
            if( fulfillsCondition(data.get(header), operator, value) ) {
                result.add(data);
            }
        }

        return result;
    }

    // TODO clean this up, rework it
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
            return leftValue.equals(rightValue);
        }
        if( "<=".equals(operator) || "<".equals(operator) ){
            // TODO
        }
        if( "!=".equals(operator) ){
            return !leftValue.equals(rightValue);
        }

        return false;
    }

}
