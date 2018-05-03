package com.Avengers.app.MachineLearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.Avengers.app.Interface_Module;
import com.Avengers.app.Framework_Module;
import com.Avengers.app.MachineLearning.FeatureExtraction.Feature;
import com.Avengers.app.MachineLearning.FeatureExtraction.FeatureExtractor;

public class MachineLearningModule extends Interface_Module{

    /* Framework module used to get more data, and notify the framework when something happened */
    private Framework_Module frameworkModule;

    private FeatureExtractor featureExtractor = new FeatureExtractor();

    ArrayList<Map<String, Double>> allFeaturesVector = new ArrayList<>();

    private static final int sampleSize = 10;

    public MachineLearningModule(String moduleName, Framework_Module frameworkModule){
        this.moduleName = moduleName;
        this.frameworkModule = frameworkModule;
    }

    public void run() {
        ArrayList<Map<String, Integer>> sampleMaxRequestsAppIds = new ArrayList<>();

        while(true) {
            ArrayList<Map<String, String>> logData = new ArrayList<>();
            frameworkModule.getData(moduleName, logData, sampleSize);

            Map<String, Double> featureVector = featureExtractor.createFeaturesVector(logData, sampleSize);

            allFeaturesVector.add(featureVector);

        }
    }
}
