package com.Avengers.app.Security;


import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {

    ArrayList<String> parseLine(Map<String, String> logLine){
        String originValue = logLine.get("origin");
        String domainName;
        String startTime;
        String responseTime;

        ArrayList<String> outArrayList = new ArrayList<>();

        if(originValue == null){
            return null;
        }

        /* Only interested in logs from the GoRouter */
        if(!originValue.equals("\"gorouter\"")){
            return null;
        }

        String logMessage = logLine.get("message");

        if(logMessage == null){
            return null;
        }

        domainName = getDomainName(logMessage);

        if(domainName == null){
            return null;
        }

        startTime = getStartTime(logMessage);

        if(startTime == null){
            return null;
        }

        responseTime = getResponseTime(logMessage);

        if(responseTime == null){
            return null;
        }

        outArrayList.add(domainName);
        outArrayList.add(startTime);
        outArrayList.add(responseTime);

        return outArrayList;
    }

    private String getDomainName(String logMessage){
        Pattern pattern = Pattern.compile("\"([-a-zA-Z_0-9.]*) .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

    private String getStartTime(String logMessage){
        Pattern pattern = Pattern.compile("\".* - \\[(.*)\\] .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

    private String getResponseTime(String logMessage){
        Pattern pattern = Pattern.compile("\".*?response_time:0[.](.*?) .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

}
