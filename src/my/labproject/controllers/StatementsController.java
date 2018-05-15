package my.labproject.controllers;

import my.labproject.Config;
import my.labproject.Config.StatementPatterns;
import my.labproject.utils.PathUtil;
import my.labproject.utils.PatternMatcher;
import my.labproject.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *  Statements implementation
 */

public class StatementsController {

    private final Config config = new Config();
    private final LoggerController log = new LoggerController();
    private final FileController fileControl = new FileController();
    private final PatternMatcher patternMatcher = new PatternMatcher();
    private final PathUtil pathUtil = new PathUtil();


    public boolean createDatabase(String query){
        String dbName = patternMatcher.retrieve(StatementPatterns.CreateDatabase, query, 1);
        String path = pathUtil.getDbPath(dbName);

        if( path != null && fileControl.create( path, "d") ){
            log.INFO("Successfully created database: \""+dbName+"\"");
        } else {
            log.ERROR("Could not create database \""+dbName+"\"");
            return false;
        }
        return true;
    }

    public boolean createTable(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.CreateTable, query, 1);
        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) ){
            log.ERROR("Cannot create table, please select DB to use first!");
            return false;
        }

        String paramsComponent = patternMatcher.retrieve(StatementPatterns.CreateTable, query, 2);
        ArrayList<String> params = new ArrayList<>(Arrays.asList(paramsComponent.split(",")));
        if( fileControl.create(path, "f") && fileControl.saveLineToFile(path, params)){
            log.INFO("Successfully created table: \""+tableName+"\"");
        } else {
            log.ERROR("Could not create table \""+tableName+"\"");
            return false;
        }
        return true;
    }

    public boolean dropDatabase(String query){
        String dbName = patternMatcher.retrieve(StatementPatterns.DropDatabase, query, 1);
        String path = pathUtil.getDbPath(dbName);
        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path) ){
            log.ERROR("Cannot drop database \""+dbName+"\"! It may not exist!");
            return false;
        }

        if( dbName.equals(config.getUsedDatabase()) ){
            config.setUsedDatabase(null);
        }

        if( fileControl.deleteAll(path) ){
            log.INFO("Successfully dropped database \""+dbName+"\"");
        } else {
            log.ERROR("Error occurred while dropping database!");
            return false;
        }
        return true;
    }

    public boolean dropTable(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.DropTable, query, 1);
        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path) ){
            log.ERROR("Cannot drop table \""+tableName+"\"! It may not exist!");
            return false;
        }

        if( fileControl.deleteAll(path) ){
            log.INFO("Successfully dropped table \""+tableName+"\"");
        } else {
            log.ERROR("Error occurred while dropping table!");
            return false;
        }
        return true;
    }

    public boolean showDatabases(){
        String usedWorkspace = config.getUsedWorkspace();
        if(StringUtil.isNullOrEmpty(usedWorkspace)){
            log.ERROR("Cannot show databases, no workspace active!");
            return false;
        }

        ArrayList<String> databases = fileControl.listFiles(usedWorkspace);
        if( databases.size() == 0 ){
            log.INFO("No databases in workspace \""+usedWorkspace+"\"");
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

    public boolean showTables(){
        String path = pathUtil.getDbPath(config.getUsedDatabase());
        if( StringUtil.isNullOrEmpty(path) ){
            log.ERROR("Cannot show tables, please select DB to use first!");
            return false;
        }

        ArrayList<String> tables = fileControl.listFiles(path);
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

    public boolean showTable(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.ShowTable, query, 1);
        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) ){
            log.ERROR("Cannot show table \""+tableName+"\", please select DB to use first!");
            return false;
        }

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

    public boolean use(String query){
        String dbName = patternMatcher.retrieve(StatementPatterns.Use, query, 1);
        if( StringUtil.isNullOrEmpty(config.getUsedWorkspace()) ){
            log.ERROR("Workspace field is empty!");
            return false;
        }

        String path = pathUtil.getDbPath(dbName);
        if (!StringUtil.isNullOrEmpty(path) && fileControl.exists(path)) {
            config.setUsedDatabase(dbName);
            log.INFO("Using database \"" + dbName + "\"");
        } else {
            log.ERROR("Could not use database \"" + dbName + "\". Database does not exists!");
            return false;
        }

        return true;
    }

    public boolean using(){
        log.INFO("Currently use: \""+config.getUsedDatabase()+"\" database and \""+config.getUsedWorkspace()+"\" workspace.");
        return true;
    }

    public boolean select(String query){
        String headersToGetComponent = patternMatcher.retrieve(StatementPatterns.Select, query, 1);
        String tableName = patternMatcher.retrieve(StatementPatterns.Select, query, 3);

        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path) ) {
            log.ERROR("Could not select data from table \""+tableName+"\", because it does not exist!");
            return false;
        }

        ArrayList<String> tableHeaders = fileControl.readHeader(path);
        ArrayList<String> headersToGet = new ArrayList<>(Arrays.asList(headersToGetComponent.split(",")));
        StringBuilder sb = new StringBuilder();
        if(headersToGet.size() == 1 && headersToGet.get(0).trim().equals("*")) {
            headersToGet = tableHeaders;
        } else {
            for (String header : headersToGet) {
                if (!tableHeaders.contains(header.trim())) {
                    log.ERROR("Table \"" + tableName + "\" does not contain field \"" + header.trim() + "\"!");
                    return false;
                }
            }
        }

        for( String header : headersToGet){
            sb.append(header.trim()).append(Config.Constants.SELECT_DATA_SEPARATOR);
        }
        sb.delete(sb.lastIndexOf(Config.Constants.SELECT_DATA_SEPARATOR), sb.length()).append("\n       ");

        ArrayList<HashMap<String, String>> data = fileControl.readData(path, headersToGet);
        if(query.toUpperCase().matches("^.* WHERE \\w+ ?(==|!=) ?.+;?$")) {
            data = where(fileControl.readData(path), query);
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

    public boolean insert(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.Insert, query, 1);
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList( patternMatcher.retrieve(StatementPatterns.Insert, query, 2).split(",")));
        ArrayList<String> data = new ArrayList<String>(Arrays.asList( patternMatcher.retrieve(StatementPatterns.Insert, query, 4).split(",")));

        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path)){
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

        ArrayList<HashMap<String, String>> dataInTable =  fileControl.readData(path);
        for(HashMap<String, String> row : dataInTable){
            for(String header : headers){
                if( row.get(header).equals(dataSet.get(header)) ){
                    log.ERROR("Could not insert data! Such row already exists!");
                    return true;
                }
            }
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

    public boolean update(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.Update, query,1);
        String path = pathUtil.getTablePath(tableName);

        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path) ){
            log.ERROR("Table does not exist");
            return false;
        }

        String setPart = patternMatcher.retrieve(StatementPatterns.Update, query,2);
        ArrayList<String> setList = new ArrayList<String>(Arrays.asList(setPart.trim().split(",")));
        HashMap<String, String> setMap = new HashMap<>();
        for(String set : setList){
            setMap.put(set.split("=")[0].trim(), set.split("=")[1].trim());
        }

        String conditionPart = query.split("WHERE|where")[1];
        String headerWhere = conditionPart.trim().split("(==|!=)")[0].trim();
        String operator = patternMatcher.retrieve("(==|!=)", conditionPart);
        String value = conditionPart.trim().split("(==|!=)")[1].trim();

        ArrayList<String> headers = fileControl.readHeader(path);
        ArrayList<HashMap<String, String>> data = fileControl.readData(path);

        if( StringUtil.isNullOrEmpty(headerWhere) || StringUtil.isNullOrEmpty(operator) ||
            StringUtil.isNullOrEmpty(value) || headers == null || data == null) {
            return false;
        }

        for( HashMap<String, String> row : data ) {
            if (row.containsKey(headerWhere) && fulfillsCondition(row.get(headerWhere), operator, value)) {
                for (String key : setMap.keySet()) {
                    row.put(key, setMap.get(key));
                }
            }
        }
        return fileControl.insertAllData(path, data, headers);
    }

    public boolean delete(String query){
        String tableName = patternMatcher.retrieve(StatementPatterns.Delete, query, 1);
        String path = pathUtil.getTablePath(tableName);
        if( StringUtil.isNullOrEmpty(path) || !fileControl.exists(path) ){
            log.ERROR("Table does not exist");
            return false;
        }

        String conditionPart = query.split("WHERE|where")[1];
        String headerWhere = conditionPart.trim().split("(==|!=)")[0].trim();
        String operator = patternMatcher.retrieve("(==|!=)", conditionPart);
        String value = conditionPart.trim().split("(==|!=)")[1].trim();

        ArrayList<String> headers = fileControl.readHeader(path);
        ArrayList<HashMap<String, String>> data = fileControl.readData(path);

        if( StringUtil.isNullOrEmpty(headerWhere) || StringUtil.isNullOrEmpty(operator) ||
                StringUtil.isNullOrEmpty(value) || headers == null || data == null) {
            return false;
        }

        ArrayList<HashMap<String, String>> toDelete = new ArrayList<>();
        for( HashMap<String, String> row : data ) {
            if (row.containsKey(headerWhere) && fulfillsCondition(row.get(headerWhere), operator, value)) {
                toDelete.add(row);
            }
        }
        data.removeAll(toDelete);
        return fileControl.insertAllData(path, data, headers);
    }

    public boolean exit(){
        return true;
    }

    /*
     *  HELPER FUNCTIONS
     */
    private ArrayList<HashMap<String, String>> where(ArrayList<HashMap<String, String>> allData, String query){
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        String conditionPart = query.split("WHERE|where")[1];
        String header = conditionPart.trim().split("(==|!=)")[0].trim();
        String operator = patternMatcher.retrieve("(>|>=|==|<=|<|!=)", conditionPart);
        String value = conditionPart.trim().split("(==|!=)")[1].trim();

        log.DEBUG("Entering where clause");
        if( !allData.get(0).containsKey(header) ){
            log.ERROR("Table does not contain field \""+header+"\"!");
            return result;
        } else if ( StringUtil.isNullOrEmpty(conditionPart) || StringUtil.isNullOrEmpty(header) ||
                    StringUtil.isNullOrEmpty(operator) || StringUtil.isNullOrEmpty(value) ){
            log.ERROR("Missing data in WHERE clause !");
            return result;
        }

        for( HashMap<String, String> data : allData){
            if( fulfillsCondition(data.get(header), operator, value) ) {
                result.add(data);
            }
        }

        return result;
    }

    private boolean fulfillsCondition(String leftValue, String operator, String rightValue){

        if( "==".equals(operator) ){
            return leftValue.equals(rightValue);
        }
        if( "!=".equals(operator) ){
            return !leftValue.equals(rightValue);
        }

        return false;
    }

}
