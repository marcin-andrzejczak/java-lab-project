package my.labproject.controllers;

import my.labproject.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FileController {

    private static final LoggerController log = Config.Constants.LOGGER;

    public boolean create(String pathname, String mode){
        Boolean result = false;

        try {
            File dir = new File(pathname);
            switch (mode) {
                case "dr":
                    result = dir.mkdirs();
                    break;
                case "d":
                    result = dir.mkdir();
                    break;
                case "f":
                    result = dir.createNewFile();
                    break;
                default:
                    log.ERROR("Could not create file/directory. Wrong mode!");
                    break;
            }
        } catch( Exception ex ){
            log.ERROR("Exception occurred!\n"+ex.getMessage());
        }

        return result ;
    }

    public boolean exists(String dirname){
        try {
            if ( "".equals(dirname) ) return false;

            File file = new File(dirname);
            if ( file.exists() ) return true;

        }catch (NullPointerException ex){
            log.ERROR("Exception FOUND: "+ex.getMessage()+"\n"+ex.getCause()+"\n"+ Arrays.toString(ex.getStackTrace()));
        }

        return false;
    }

    public boolean deleteAll(String path) {
        if( "".equals(path) ) return false;

        File dir = new File(path);
        File[] allContents = dir.listFiles();

        if (allContents != null) {
            for (File file : allContents) {
                if( file == null ) continue;
                deleteAll(file.getPath());
            }
        }

        return dir.delete();
    }

    public ArrayList<String> listFiles(String path) {
        if( "".equals(path) ) return new ArrayList<>();

        File dir = new File(path);
        File[] allContents = dir.listFiles();
        ArrayList<String> fileList = new ArrayList<>();

        if( allContents != null ) {
            for( File file : allContents ){
                if( file == null ) continue;
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    public boolean saveLineToFile(String path, ArrayList<String> decomposedLine){
        if( "".equals(path) || decomposedLine.isEmpty() || !exists(path) ) return false;

        StringBuilder sb = new StringBuilder();
        for(String component : decomposedLine){
            if( "".equals(component) ) return false;
            sb.append(component.trim()).append(Config.Constants.DATA_DELIMITER);
        }
        sb.deleteCharAt(sb.lastIndexOf(Config.Constants.DATA_DELIMITER));
        return saveLineToFile(path, sb.toString());
    }

    public boolean saveLineToFile(String path, String textLine){
        try {
            if( "".equals(path) || "".equals(textLine) || !exists(path )) return false;

            PrintWriter output = new PrintWriter(new FileWriter(path, true));
            output.println(textLine);
            output.close();

        } catch (IOException ex){
            log.ERROR("IOException: "+ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertDataWithHeaders(String path, HashMap<String, String> dataSet ){
        if( "".equals(path) || dataSet.isEmpty() || !exists(path) ) return false;

        ArrayList<String> headers = readHeader( path );
        StringBuilder sb = new StringBuilder();

        if(headers.isEmpty())
            return false;

        for( String header : headers ){
            if( !dataSet.containsKey(header) ) {
                return false;
            }
            sb.append(dataSet.get(header)).append(Config.Constants.DATA_DELIMITER);
        }
        sb.deleteCharAt(sb.lastIndexOf(Config.Constants.DATA_DELIMITER));

        return saveLineToFile(path, sb.toString());
    }

    public ArrayList<String> readHeader(String path){
        ArrayList<String> headers = new ArrayList<>();
        File file = new File(path);
        String line;
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            line = br.readLine();
            br.close();

            if( !"".equals(line) )
                headers = new ArrayList<>(Arrays.asList(line.split(Config.Constants.DATA_DELIMITER)));

        } catch ( IOException ex ){
            return headers;
        }

        return headers;
    }

    public ArrayList<HashMap<String, String>> readData(String path) {
        return readData(path, null);
    }

    public ArrayList<HashMap<String, String>> readData(String path, ArrayList<String> headersToGet){
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        ArrayList<String> headers = readHeader(path);

        try (BufferedReader br = new BufferedReader(new FileReader( new File(path)))){
            String line;

            br.readLine();
            while( (line = br.readLine()) != null ) {
                HashMap<String, String> row = new HashMap<>();
                ArrayList<String> rowData = new ArrayList<>(Arrays.asList(line.split(Config.Constants.DATA_DELIMITER)));
                for(String param : rowData){
                    if( headersToGet == null || headersToGet.contains(headers.get(rowData.indexOf(param))) )
                        row.put(headers.get(rowData.indexOf(param)), param );
                }
                data.add( row );
            }
        } catch ( IOException ex ){
            return data;
        }

        return data;
    }

    public boolean deleteLines(String path, HashMap<String, String> conditions){
        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            StringBuffer inputBuffer = new StringBuffer();
            ArrayList<String> headers = readHeader(path);
            HashMap<String, String> parsedLine;
            String line;

            while ((line = file.readLine()) != null) {
                parsedLine = parseLine(line, headers);
                inputBuffer.append(line).append('\n');
            }
            String inputStr = inputBuffer.toString();

            file.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public HashMap<String, String> parseLine(String line, ArrayList<String> headers){
        HashMap<String, String > result = new HashMap<>();
        ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(Config.Constants.DATA_DELIMITER)));

        if( headers.size() == lineComponents.size()) {
            for (String header : headers) {
                result.put(header, lineComponents.get(headers.indexOf(header)));
            }
        }
        return result;
    }

}
