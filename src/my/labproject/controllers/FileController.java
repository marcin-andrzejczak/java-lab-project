package my.labproject.controllers;

import my.labproject.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
            if (dirname.isEmpty())
                return false;

            File file = new File(dirname);

            if (file.isDirectory())
                return true;

            if (file.exists())
                return true;

        }catch (NullPointerException ex){
            log.ERROR("Exception FOUND: "+ex.getMessage()+"\n"+ex.getCause()+"\n"+ Arrays.toString(ex.getStackTrace()));
        }

        return false;
    }

    public boolean deleteAll(String path) {
        File dir = new File(path);
        File[] allContents = dir.listFiles();

        if (allContents != null) {
            for (File file : allContents) {
                deleteAll(file.getPath());
            }
        }

        return dir.delete();
    }

    public ArrayList<String> listFiles(String path) {
        File dir = new File(path);
        File[] allContents = dir.listFiles();
        ArrayList<String> fileList = new ArrayList<>();

        if( allContents != null ) {
            for( File file : allContents ){
                fileList.add(file.getName());
            }
        }
        return fileList;
    }



//    public boolean saveToFile(String path, Vector<Double> vec){
//        try {
//            FileOutputStream output = new FileOutputStream(path);
//            String vecString = vec.toString();
//            for(int loopIterator=0; loopIterator<vecString.length(); loopIterator++) {
//                output.write(vecString.toCharArray()[loopIterator]);
//            }
//            output.close();
//        } catch (IOException ex){
//            System.out.println("IOException: "+ex.getMessage());
//            return false;
//        }
//        System.out.println("Vector successfully saved at \""+path+"\"!");
//        return true;
//    }

}
