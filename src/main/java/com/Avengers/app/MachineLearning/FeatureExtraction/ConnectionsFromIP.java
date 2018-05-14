package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionsFromIP implements Feature{

    /* The name of the feature */
    private static final String featureName = "maxConnectionsFromIp";

    private Double featureValue = null;

    public boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines,
                                    Map<String, Feature> featureNameToObject){

        /* A mapping between the ip, and the number of connections it made in the sample */
        Map<String, Integer> ipToNumOfConnections = new HashMap<>();

        if(number_of_lines == 0){
            return false;
        }

        for(Map<String, String> logLine: logData){
            if(!handleLogLine(logLine, ipToNumOfConnections)){
                return false;
            }
        }

        featureValue = (double)getMaxConnections(ipToNumOfConnections)/number_of_lines;

        return true;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    private Integer getMaxConnections(Map<String, Integer> ipToNumOfConnections){
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

    private boolean handleLogLine(Map<String, String> logLine, Map<String, Integer> ipToNumOfConnections){
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
