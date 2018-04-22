package my.labproject.utils;

import my.labproject.Config;
import my.labproject.controllers.LoggerController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class CommandMatcher {

    private PatternMatcher patternMatcher = new PatternMatcher();
    private LoggerController log = new LoggerController();

    public boolean matchesAnyAvailable(String query, Statements statements){
        try {
            Field[] patterns = Config.StatementPatterns.class.getDeclaredFields();
            for (Field pattern : patterns) {
                if ( patternMatcher.match( (Pattern) pattern.get(null) , query) ) {
                    String methodName = Character.toLowerCase(pattern.getName().charAt(0)) + pattern.getName().substring(1);
                    return executeMethod(methodName, statements, query);
                }
            }
        } catch ( IllegalAccessException ex ){
            return false;
        }

        return false;
    }

    private boolean executeMethod(String methodName, Statements statements, String query){
        boolean result;
        boolean paramsPresent = false;

        if (methodName == null){
            return false;
        }

        for( Method m : Statements.class.getDeclaredMethods() ){
            if( m.getName().equals(methodName) ){
                if( m.getGenericParameterTypes().length > 0 ){
                    paramsPresent = true;
                }
            }
        }

        try {
            Method methodToDo;
            if( paramsPresent ){
                methodToDo = Statements.class.getDeclaredMethod(methodName, String.class);
                result = (boolean) methodToDo.invoke(statements, query);
            } else {
                methodToDo = Statements.class.getDeclaredMethod(methodName);
                result = (boolean) methodToDo.invoke(statements);
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.ERROR("Exception occurred!");
            e.printStackTrace();
            return false;
        }

        return result;
    }
}
