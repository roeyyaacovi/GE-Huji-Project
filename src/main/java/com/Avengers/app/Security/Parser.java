package com.Avengers.app.Security;


import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String example = "message:\"cert-cf3.run.aws-usw02-dev.ice.predix.io - [2017-12-10T11:34:18.316+0000] \\\"GET /cs/v1/certificate/default-predix-performance/620035de-4262-496f-b8eb-05f4f9083482/publickey HTTP/1.1\\\" 200 0 592 \\\"-\\\" \\\"Apache-HttpClient/4.5.2 (Java/1.8.0_152)\\\" \\\"10.72.131.25:52896\\\" \\\"10.72.134.237:63507\\\" x_forwarded_for:\\\"-\\\" x_forwarded_proto:\\\"http\\\" vcap_request_id:\\\"476623cf-7089-4d5d-69ae-8dc26e107c31\\\" response_time:0.003320452 app_id:\\\"7ed06b66-7a25-4ed3-8731-a944ea71751b\\\" app_index:\\\"-\\\" x_b3_traceid:\\\"deab9b2b835baac9\\\" x_b3_spanid:\\\"deab9b2b835baac9\\\" x_b3_parentspanid:\\\"-\\\"\\n\" message_type:OUT timestamp:1512905658320085026 app_id:\"7ed06b66-7a25-4ed3-8731-a944ea71751b\" source_type:\"RTR\" source_instance:\"4\"";

    public TimeSlot parseLine(Map<String, String> logLine){
//        String originValue = logLine.get("origin");
        String domainName;
        String startTime;
        String responseTime;

//        if(originValue == null){
//            return null;
//        }

//        /* Only interested in logs from the GoRouter. */
//        if(!originValue.equals("gorouter")){
//            return null;
//        }
//
//        String logMessage = logLine.get("logMessage");
//
//        if(logMessage == null){
//            return null;
//        }
//
//        domainName = getDomainName(logMessage);

        domainName = getDomainName(example);

        if(domainName == null){
            return null;
        }

        startTime = getStartTime(example);

        if(startTime == null){
            return null;
        }

        responseTime = getResponseTime(example);

        if(responseTime == null){
            return null;
        }

        return null;
    }

    private String getDomainName(String logMessage){
        Pattern pattern = Pattern.compile("message:\"([-a-zA-Z_0-9.]*) .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

    private String getStartTime(String logMessage){
        Pattern pattern = Pattern.compile("message:\".* - \\[(.*)\\] .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

    private String getResponseTime(String logMessage){
        Pattern pattern = Pattern.compile("message:\".*?response_time:(.*?) .*");

        Matcher matcher = pattern.matcher(logMessage);

        if(!matcher.find()){
            return null;
        }

        return matcher.group(1);
    }

}
