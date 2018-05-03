package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.util.ArrayList;
import java.util.Map;

public interface Feature {
    boolean calculateFeature(ArrayList<Map<String, String>> logData, int number_of_lines,
                             Map<String, Feature> featureNameToObject);

    Double getFeatureValue();

    String getFeatureName();
}
