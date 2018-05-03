package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaxRequests implements Feature {

    /* The name of the feature */
    private static final String featureName = "maxRequests";

    private Double featureValue = null;

    private String appName = null;

    public boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines,
                                    Map<String, Feature> featureNameToObject){


        Map<String, Double> all_app_ids = new HashMap<>();
        for (Map<String, String> line : logData){
            String app_id_to_check = line.get("app_id");
            if(all_app_ids.containsKey(app_id_to_check))
            {
                all_app_ids.put(app_id_to_check, all_app_ids.get(app_id_to_check)+1);
            }
            else
            {
                all_app_ids.put(app_id_to_check, (double)1);
            }
        }
        double maxval = 0;
        Map.Entry<String, Double> retEntry = null;
        for (Map.Entry<String, Double> entry : all_app_ids.entrySet())
        {
            if (entry.getValue() > maxval)
            {
                retEntry = entry;
                maxval = entry.getValue();
            }
        }

        featureValue = retEntry.getValue();
        appName = retEntry.getKey();

        return true;
    }


    public String getAppName(){
        return appName;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName(){
        return featureName;
    }
}
