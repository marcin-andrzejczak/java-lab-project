package my.labproject.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import my.labproject.logger.Logger;

public class DatabaseCliController {

    private String databasePath;
    private Logger log;
    private String prompt = " -> ";

    public DatabaseCliController(){
        this("/tmp/labDatabase");
    }

    public DatabaseCliController(String path){
        this.log = new Logger();
        this.databasePath = path;
    }

    public void run(){
        String command = "";

        do{
            command = readCommand();
            log.INFO("Entered command: ".concat(command));
            interpret(command);
        } while ( !command.toUpperCase().equals("EXIT") );
    }

    private String readCommand(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String query = "";

        try {
            System.out.print(this.prompt);
            query = reader.readLine();
        } catch (IOException ex) {
            log.ERROR(ex.getMessage());
        }

        return query;
    }

    private void interpret(String query){
        String[] compounds = query.split(" ");

    }

}
