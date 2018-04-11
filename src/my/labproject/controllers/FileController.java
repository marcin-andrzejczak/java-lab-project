package my.labproject.controllers;

import my.labproject.Config;

import java.io.File;

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

    public boolean exis(String dirname){
        log.DEBUG("CODOKURWY");
        log.WARN("1");
        if( dirname.isEmpty() ){
            log.ERROR("Cannot check existence, directory argument is empty!");
            return false;
        }
        log.DEBUG("KURWA CZEMU TO NIE DZIALA DO CHUJA");
        log.WARN("2");
        File file = new File(dirname);
        log.WARN("3");
        try {
            if (file.isDirectory()) {
                log.DEBUG("Directory \"" + dirname + "\" exists.");
                return true;
            }
            log.WARN("4");
            if (file.exists()) {
                log.DEBUG("File \"" + dirname + "\" exists.");
                return true;
            }
            log.WARN("5");
        } catch ( NullPointerException ex ){
            //log.ERROR("Null pointer exception occurred!\n"+ex.getMessage());
        } catch ( Exception ex ) {
            log.ERROR("Exception occurred!\n"+ex.getMessage());
        }
        log.WARN("6");
        log.ERROR("File/directory existence check failed.");
        log.WARN("7");
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
        log.DEBUG("Deleting: "+dir.getPath());
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
