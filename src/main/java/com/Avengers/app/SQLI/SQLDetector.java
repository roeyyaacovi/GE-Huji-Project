package com.Avengers.app.SQLI;

import java.util.ArrayList;
import java.util.List;

public class SQLDetector {
    private Training training;

    private enum ML_DECISION{
        BENIGN,
        MALICIOUS
    }

    private ML_DECISION makeDecision(ArrayList<ArrayList<Double>> maliciousProbabilityVector,
                                       ArrayList<ArrayList<Double>> vanillaProbabilityVector, ArrayList<Integer> sample){
        Double sampleVanillaProbability = null;
        Double sampleMaliciousProbability = null;

        for(int i=0; i<sample.size(); i++){
            if(null == sampleVanillaProbability){
                sampleVanillaProbability = vanillaProbabilityVector.get(i).get(Training.getBucket(sample.get(i)));
                sampleMaliciousProbability = maliciousProbabilityVector.get(i).get(Training.getBucket(sample.get(i)));

                continue;
            }

            sampleVanillaProbability = sampleVanillaProbability*vanillaProbabilityVector.get(i).get(Training.getBucket(sample.get(i)));
            sampleMaliciousProbability = sampleMaliciousProbability*maliciousProbabilityVector.get(i).get(Training.getBucket(sample.get(i)));
        }

        if(sampleMaliciousProbability == null || sampleVanillaProbability == null){
            return null;
        }

        if(sampleVanillaProbability > sampleMaliciousProbability){
            return ML_DECISION.BENIGN;
        }

        return ML_DECISION.MALICIOUS;

    }

    private Double test(List<ArrayList<Integer>> testingMalicious, List<ArrayList<Integer>> testingVanilla){
        int totalCorrect = 0;
        int totalNumberOfSamples = testingMalicious.size() + testingVanilla.size();

        for(ArrayList<Integer> vanillaSample : testingVanilla){
            if(ML_DECISION.BENIGN == makeDecision(training.getMaliciousProbabilityVector(),
                    training.getVanillaProbabilityVector(), vanillaSample)){
                totalCorrect += 1;
            }
        }

        for(ArrayList<Integer> maliciousSample : testingMalicious){
            if(ML_DECISION.MALICIOUS == makeDecision(training.getMaliciousProbabilityVector(),
                    training.getVanillaProbabilityVector(), maliciousSample)){
                totalCorrect += 1;
            }
        }

        return (double)totalCorrect/totalNumberOfSamples;
    }

    public void run(){
        FeatureBuilder featureBuilder = new FeatureBuilder();
        featureBuilder.createFeatureVectors();

        training = new Training();
        training.train(featureBuilder.getTrainingMalicious(), featureBuilder.getTrainingVanilla());

        double totalCorrect = test(featureBuilder.getTestingMalicious(), featureBuilder.getTestingVanilla());

        System.out.println(totalCorrect);
    }
}
