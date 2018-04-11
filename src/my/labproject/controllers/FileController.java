package my.labproject.controllers;

import my.labproject.Config;

import java.io.File;
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
            if (dirname.isEmpty()) {
                return false;
            }

            File file = new File(dirname);

            if (file.isDirectory()) {

                return true;
            }
            log.DEBUG("CODOKURWYSIETUDZIEJE");
            if (file.exists()) {

                return true;
            }
        }catch (NullPointerException ex){
            System.out.println("Exception FOUND: "+ex.getMessage()+"\n"+ex.getCause()+"\n"+ Arrays.toString(ex.getStackTrace()));
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
