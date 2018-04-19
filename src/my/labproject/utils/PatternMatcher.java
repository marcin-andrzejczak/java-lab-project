package my.labproject.utils;

import my.labproject.Config.StatementPatterns;
import my.labproject.controllers.LoggerController;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

    private final LoggerController log = new LoggerController();

    public boolean match(Pattern pattern, String query){
        Matcher m = pattern.matcher(query);
        return m.matches();
    }

    // TODO
    public String retrieve(String pattern, String query){
        return retrieve(Pattern.compile(pattern), query, 1);
    }

    public String retrieve(Pattern pattern, String query, Integer position){
        Matcher m = pattern.matcher(query);
        return m.find() ? m.group(position) : null;
    }

    public Integer getGroupsCount(Pattern pattern, String query){
        Matcher m = pattern.matcher(query);
        return m.groupCount();
    }

    public boolean matchesAnyAvailable(String query){
        ArrayList<Pattern> patternsAvailable = new ArrayList<>();

        try {
            Field[] fields = StatementPatterns.class.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    patternsAvailable.add( (Pattern) f.get(null));
                }
            }
        } catch ( IllegalAccessException ex ){
            return false;
        }

        for( Pattern pattern : patternsAvailable ){
            if( match(pattern, query) )
                return true;
        }

        return false;
    }

}
