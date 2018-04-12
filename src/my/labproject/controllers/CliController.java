package my.labproject.controllers;

import my.labproject.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliController {

    private final Config config = Config.Constants.CHANGEABLE;
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

    public boolean askYesNo(String question){
        String answer = "";
        int triesNumber = Config.Constants.CLI_TRIES_NUMBER;

        do {
            log.INFO(question);
            answer = readCommand();
            String s = answer.toUpperCase();
            if ("YES".equals(s) || "Y".equals(s)) {
                return true;

            } else if ("NO".equals(s) || "N".equals(s)) {
                return false;

            } else {
                log.INFO("Answer not recognized!");
            }

        } while( --triesNumber > 0 );

        log.INFO("Could not recognize answer too many times!. Using already existing workspace: "+config.getUsedWorkspace());
        return false;
    }

}
