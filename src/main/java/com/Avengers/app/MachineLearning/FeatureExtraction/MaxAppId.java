package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaxAppId implements Feature {

    /* The name of the feature */
    private static final String featureName = "maxAppId";

    /* Mapping between an app id, and a number that represents it so it could be put in the feature vector */
    private Map<String, Double> sampleMaxRequestsAppIds = new HashMap<>();

    private Double featureValue = null;

    public boolean calculateFeature(ArrayList<Map<String, String>> logData,
                                    int number_of_lines, Map<String, Feature> featureNameToObject){

        /* To calculate, need the value of the MaxRequests feature */
        MaxRequests maxRequestsFeature = (MaxRequests)featureNameToObject.get("maxRequests");

        if(maxRequestsFeature == null){
            return false;
        }

        /* If the key is not in the map, then we don't care and just return that its ok */
        if(sampleMaxRequestsAppIds.containsKey(maxRequestsFeature.getAppName())){
            featureValue = sampleMaxRequestsAppIds.get(maxRequestsFeature.getAppName());
            return true;
        }

        sampleMaxRequestsAppIds.put(maxRequestsFeature.getAppName(), (double)sampleMaxRequestsAppIds.size()+1);

        featureValue = sampleMaxRequestsAppIds.get(maxRequestsFeature.getAppName());

        return true;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public static String  getFeatureName(){
        return featureName;
    }
}


