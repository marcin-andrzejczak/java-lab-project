package my.labproject.controllers;

import my.labproject.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliController {

    private final String prompt = Config.Constants.PROMPT;
    private final LoggerController log = Config.Constants.LOGGER;

    public String readCommand(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String command = "";

        try {
            System.out.print(this.prompt);
            command = reader.readLine();
            log.DEBUG("Entered command: "+command);
        } catch (IOException ex) {
            log.ERROR(ex.getMessage());
        }

        return command;
    }

}
