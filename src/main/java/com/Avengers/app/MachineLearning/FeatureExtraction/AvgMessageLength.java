package com.Avengers.app.MachineLearning.FeatureExtraction;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.Map;

public class AvgMessageLength implements Feature {

    /* The name of the feature */
    private static final String featureName = "avgMsgLength";

    private Double featureValue = null;

    public boolean calculateFeature(ArrayList<Map<String, String>> logData,
                                    int number_of_lines, Map<String, Feature> featureNameToObject){
        int length_sum = 0;

        for (Map<String, String> line : logData){
            length_sum += line.get("message").length();
        }

        featureValue = (double)length_sum / number_of_lines;

        return true;
    }

    public Double getFeatureValue(){
        return featureValue;
    }

    public String getFeatureName(){
        return featureName;
    }
}
