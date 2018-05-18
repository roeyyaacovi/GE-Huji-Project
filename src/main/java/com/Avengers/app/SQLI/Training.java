package com.Avengers.app.SQLI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

class Training {

    /* ML variables */
    private static final int NUM_OF_BUCKETS = 15;
    private static final int BUCKET_SIZE = 3;

    /* Final probability vectors */
    private ArrayList<ArrayList<Double>> maliciousProbabilityVector;
    private ArrayList<ArrayList<Double>> vanillaProbabilityVector;

    private ArrayList<Integer> createEmptyArray(int size){
        ArrayList<Integer> emptyArray = new ArrayList<>();

        for(int i=0; i<size; i++){
            emptyArray.add(0);
        }

        return emptyArray;
    }

    private ArrayList<ArrayList<Integer>> initFeatureCount() {
        ArrayList<ArrayList<Integer>> featureCount = new ArrayList<>();
        int numberOfFeatures = FeatureBuilder.TOKEN_LIST.length;

        for (int i = 0; i < numberOfFeatures; i++) {
            featureCount.add(createEmptyArray(NUM_OF_BUCKETS));
        }

        return featureCount;
    }


    static int getBucket(int value){
        int bucketNum = value/BUCKET_SIZE;

        if(bucketNum > NUM_OF_BUCKETS){
            bucketNum = NUM_OF_BUCKETS - 1;
        }

        return bucketNum;

    }
    private ArrayList<ArrayList<Double>> calculateProbability(List<ArrayList<Integer>> featureVectors){
       ArrayList<ArrayList<Integer>> featureCount = initFeatureCount();
       ArrayList<ArrayList<Double>> featureProbabilities = new ArrayList<>();

        /* Check how many times tokens appeared in all of the samples put together */
        for(ArrayList<Integer> featureVector : featureVectors) {
            for (int i = 0; i < featureVector.size(); i++) {
                featureCount.get(i).set(getBucket(featureVector.get(i)), featureVector.get(i) + 1);
            }
        }

        for(ArrayList<Integer> feature : featureCount){
            ArrayList<Double> featureProbability = new ArrayList<>();
            int featureSum = 0;

            /* Count the total amount of times the tokens from the token group appeared */
            for(Integer featureValue: feature){
                featureSum += featureValue;
            }

            /* Divide by the total amount in order to get percentage (probability) */
            for(int i=0; i<feature.size(); i++){
                featureProbability.add((double)feature.get(i)/featureSum);
            }

            featureProbabilities.add(featureProbability);
        }

        return featureProbabilities;
    }

    void train(List<ArrayList<Integer>> trainingMalicious, List<ArrayList<Integer>> trainingVanilla){
        maliciousProbabilityVector = calculateProbability(trainingMalicious);
        vanillaProbabilityVector = calculateProbability(trainingVanilla);
    }

    ArrayList<ArrayList<Double>> getMaliciousProbabilityVector() {
        return maliciousProbabilityVector;
    }

    ArrayList<ArrayList<Double>> getVanillaProbabilityVector() {
        return vanillaProbabilityVector;
    }
}
