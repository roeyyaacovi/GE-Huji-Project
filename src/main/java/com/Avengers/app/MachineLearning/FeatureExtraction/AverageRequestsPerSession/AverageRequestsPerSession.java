package com.Avengers.app.MachineLearning.FeatureExtraction.AverageRequestsPerSession;

import com.Avengers.app.MachineLearning.FeatureExtraction.Feature;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AverageRequestsPerSession implements Feature {

    /* The name of the feature */
    private static final String featureName = "minAvgRequestPerSession";

    private Double featureValue = null;

    private ArrayList<IpAnalyze> getIps(ArrayList<Map<String, String>> buff) {
        Set<String> allIps = new HashSet<>();
        ArrayList<IpAnalyze> ret = new ArrayList<>();

        for (Map<String, String> line : buff) {
            allIps.add(line.get("ip"));
        }

        for (String s : allIps) {
            ret.add(new IpAnalyze(s, new BigInteger("0"), 0, 0));
        }

        return ret;
    }

    @Override
    public boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines,
                                    Map<String, Feature> featureNameToObject) {
        /* Get a list of all of the IP's in the sample */
        ArrayList<IpAnalyze> allIps = getIps(logData);

        for (Map<String, String> line : logData)
        {
            /* extract the current lines ip and time stamp */
            String currentLineIp = line.get("ip");
            BigInteger currentLineTimeStamp = new BigInteger(line.get("timestamp"));

            for (IpAnalyze ipa : allIps) {
                if(ipa.getIp().equals(currentLineIp)) {
                    updateIpAnalyze(ipa, currentLineTimeStamp);
                    break;
                }
            }
        }

        featureValue = getMinAvgBetweenSessions(allIps);

        return true;
    }

    private void updateIpAnalyze(IpAnalyze ipa, BigInteger currentLineTimeStamp){
        /* If the timestamp equals zero, then this is the first time we have encountered the ip so save the timestamp */
        if (ipa.getLastTimeStamp().equals(new BigInteger("0"))) {
            ipa.setLastTimeStamp(currentLineTimeStamp);
            ipa.setNumberOfRequests(1);
            return;
        }

        BigInteger differenceBetweenLastTimeStamp = currentLineTimeStamp.subtract(ipa.getLastTimeStamp()).abs();
        ipa.setAvgBetweenRequests(((ipa.getAvgBetweenRequests() * ipa.getNumberOfRequests()) +
                differenceBetweenLastTimeStamp.doubleValue()) / (ipa.getNumberOfRequests()+1));
        ipa.setLastTimeStamp(currentLineTimeStamp);
        ipa.setNumberOfRequests(ipa.getNumberOfRequests()+1);
    }

    private Double getMinAvgBetweenSessions(ArrayList<IpAnalyze> allIps){
        Double min = 0.0;

        for (IpAnalyze ipa : allIps)
        {
            if(ipa.getAvgBetweenRequests() == 1)
                continue;

            if(0.0 == min){
                min = ipa.getAvgBetweenRequests();
            }else{
                if(ipa.getAvgBetweenRequests() < min)
                    min = ipa.getAvgBetweenRequests();
            }
        }

        return min;
    }


    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName(){
        return featureName;
    }
}
