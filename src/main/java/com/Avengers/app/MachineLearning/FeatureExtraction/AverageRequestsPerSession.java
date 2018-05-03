package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AverageRequestsPerSession implements Feature {

    /* The name of the feature */
    private static final String featureName = "minAvgRequestPerSession";

    private Double featureValue = null;

    private ArrayList<IpAnalyze> getIps(ArrayList<Map<String, String>> buff)
    {
        // insert each ip into a list of all ips
        Set<String> allIps = new HashSet<>();
        for (Map<String, String> line : buff)
        {
            allIps.add(line.get("ip"));
        }
        ArrayList<IpAnalyze> ret = new ArrayList<>();
        for (String s : allIps)
        {
            ret.add(new IpAnalyze(s, new BigInteger("0"), 0, 0));
        }
        return ret;
    }

    @Override
    public boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines, Map<String, Feature> featureNameToObject) {

            ArrayList<IpAnalyze> allIps = getIps(logData); // get a list of all ips in the sample

            for (Map<String, String> line : logData)
            {
                // extract the current line's ip and time
                String currentLineIp = line.get("ip");
                BigInteger currentLineTimeStamp = new BigInteger(line.get("timestamp"));

                // add it to the
                for (IpAnalyze ipa : allIps)
                {
                    if(ipa.getIp().equals(currentLineIp))
                    {
                        if (ipa.getLastTimeStamp().equals(new BigInteger("0"))) // the first insertion
                        {
                            ipa.setLastTimeStamp(currentLineTimeStamp);
                            ipa.setNumberOfRequests(1);
                        }
                        else
                        {
                            BigInteger differenceBetweenLastTimeStamp = currentLineTimeStamp.subtract(ipa.getLastTimeStamp()).abs();
                            ipa.setAvgBetweenRequests(((ipa.getAvgBetweenRequests() * ipa.getNumberOfRequests()) +
                                    differenceBetweenLastTimeStamp.doubleValue()) / (ipa.getNumberOfRequests()+1));
                            ipa.setLastTimeStamp(currentLineTimeStamp);
                            ipa.setNumberOfRequests(ipa.getNumberOfRequests()+1);
                        }
                        break;
                    }
                }
            }

        for (IpAnalyze ipa : allIps)
        {
            if(ipa.getAvgBetweenRequests() == 1)
                continue;

            if(null == featureValue){
                featureValue = ipa.getAvgBetweenRequests();
            }else{
                if(ipa.getAvgBetweenRequests() < featureValue)
                    featureValue = ipa.getAvgBetweenRequests();
            }
        }

        return true;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName(){
        return featureName;
    }
}
