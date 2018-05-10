package com.Avengers.app.MachineLearning.FeatureExtraction;

import com.Avengers.app.MachineLearning.FeatureExtraction.AverageRequestsPerSession.AverageRequestsPerSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeatureExtractor {
    /* Array that holds an instance of each of the feature classes */
    private Map<String, Feature> featureNameToObject = new LinkedHashMap<>();

    public FeatureExtractor(){
        buildFeatureObjects();
    }


    public ArrayList<Double> createFeaturesVector(ArrayList<Map<String, String>> sample, int sampleSize){
        ArrayList<Double> featureVector = new ArrayList<>();

        for (Feature feature : featureNameToObject.values()) {
            if(!feature.calculateFeature(sample, sampleSize, featureNameToObject)){
                return null;
            }

            featureVector.add(feature.getFeatureValue());
        }

        return featureVector;
    }

    private void buildFeatureObjects(){
        featureNameToObject.put("avgMsgLength", new AvgMessageLength());
        featureNameToObject.put("maxConnectionsFromIp", new ConnectionsFromIP());
        featureNameToObject.put("maxRequests", new MaxRequests());
        featureNameToObject.put("maxAppId", new MaxAppId());
        featureNameToObject.put("minAvgRequestPerSession", new AverageRequestsPerSession());
    }

}
