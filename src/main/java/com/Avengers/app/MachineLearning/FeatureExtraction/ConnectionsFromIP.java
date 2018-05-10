package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionsFromIP implements Feature{

    /* A mapping between the ip, and the number of connections it made in the sample */
    private Map<String, Integer> ipToNumOfConnections = new HashMap<>();

    /* The name of the feature */
    private static final String featureName = "maxConnectionsFromIp";

    private Double featureValue = null;

    public boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines,
                                    Map<String, Feature> featureNameToObject){

        if(number_of_lines == 0){
            return false;
        }

        for(Map<String, String> logLine: logData){
            if(!handleLogLine(logLine)){
                return false;
            }
        }

        featureValue = (double)getMaxConnections()/number_of_lines;

        return true;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    private Integer getMaxConnections(){
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : ipToNumOfConnections.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        if(null != maxEntry){
            return maxEntry.getValue();
        }

        return 0;
    }

    private boolean handleLogLine(Map<String, String> logLine){
        String IpAddress = logLine.get("ip");

        if(IpAddress == null){
            return false;
        }

        /* If the IP does not appear in the mapping, add it, otherwise update the number of connections it has made */
        Integer numOfConnections = ipToNumOfConnections.get(IpAddress);

        if(null == numOfConnections){
            ipToNumOfConnections.put(IpAddress, 1);
        }else {
            ipToNumOfConnections.put(IpAddress, numOfConnections + 1);
        }

        return true;
    }


}
