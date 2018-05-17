package com.Avengers.app.SQLI;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    ArrayList<String> parseLine(Map<String, String> logLine){
        String sourceTypeValue = logLine.get("source_type");
        String sqlLine = "";

        ArrayList<String> outArrayList = new ArrayList<>();

        if(sourceTypeValue == null){
            return null;
        }

        /* Only interested in logs with source type APP/PROC/WEB */
        if(!sourceTypeValue.equals("\"APP/PROC/WEB\"")){
            return null;
        }

        String logMessage = logLine.get("message");

        if(logMessage == null){
            return null;
        }

        sqlLine = getSQLPattern(logMessage);

        if(sqlLine == null){
            return null;
        }

        outArrayList.add(sqlLine);

        return outArrayList;
    }

    private String getSQLPattern(String logMessage){
        Pattern pattern = Pattern.compile("executing prepstmnt [0-9]* ([\\s-a-zA-Z_0-9.,]*)\"");
        //Pattern pattern = Pattern.compile("executing prepstmnt [0-9]* ([\\s-a-zA-Z_0-9.]*)\" .*");
        //Pattern pattern = Pattern.compile("executing prepstmnt");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

}
