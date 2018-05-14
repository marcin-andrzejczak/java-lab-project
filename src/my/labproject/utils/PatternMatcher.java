package my.labproject.utils;

import my.labproject.controllers.LoggerController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

    private final LoggerController log = new LoggerController();

    public boolean match(Pattern pattern, String query){
        Matcher m = pattern.matcher(query);
        return m.matches();
    }

    public String retrieve(String pattern, String query){
        return retrieve(Pattern.compile(pattern), query, 1);
    }

    public String retrieve(Pattern pattern, String query, Integer position){
        Matcher m = pattern.matcher(query);
        return m.find() ? m.group(position) : null;
    }

}
