package com.Avengers.app.SQLI;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    ArrayList<String> parseLine(Map<String, String> logLine){
        String sourceTypeValue = logLine.get("source_type");
        String sqlLine = "";
        String startTime = "";

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

        startTime = getStartTime(logMessage);

        if(startTime == null){
            return null;
        }

        outArrayList.add(sqlLine);
        outArrayList.add(startTime);

        return outArrayList;
    }

    private String getStartTime(String logMessage){
        Pattern pattern = Pattern.compile("\"(.*?) ");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

    private String getSQLPattern(String logMessage){
        Pattern pattern = Pattern.compile("executing prepstmnt [0-9]* (.*) ");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

}
