package com.Avengers.app.SQLI;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLDetector extends Interface_Module {
    private Training training;

    /* Files that hold the probability vectors for future use */
    private static final String maliciousProbabilityVectorFile = "maliciousProbability";
    private static final String vanillaProbabilityVectorFile = "vanillaProbability";

    /* Probability vectors from file */
    private ArrayList<ArrayList<Double>> savedMaliciousProbabilityVector;
    private ArrayList<ArrayList<Double>> savedVanillaProbabilityVector;

    /* Parser object that parses the log message */
    private Parser logParser = new Parser();

    private static final String MESSAGE_TEMPLATE = "SQL injection detected";

    private enum ML_DECISION{
        BENIGN,
        MALICIOUS
    }

    /**
     * Constructor
     */
    public SQLDetector(String moduleName, Framework_Module frameworkModule)
    {
        this.moduleName = moduleName;
        this.sync_to = frameworkModule;
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

    private ArrayList<Double> test(List<ArrayList<Integer>> testingMalicious, List<ArrayList<Integer>> testingVanilla){
        ArrayList<Double> testData = new ArrayList<>();
        int totalFalsePositives = 0;
        int totalCorrect = 0;
        int totalFalseNegatives = 0;

        int totalNumberOfSamples = testingMalicious.size() + testingVanilla.size();

        for(ArrayList<Integer> vanillaSample : testingVanilla){
            if(ML_DECISION.BENIGN == makeDecision(training.getMaliciousProbabilityVector(),
                    training.getVanillaProbabilityVector(), vanillaSample)){
                totalCorrect += 1;
            }else {
                totalFalsePositives += 1;
            }
        }

        for(ArrayList<Integer> maliciousSample : testingMalicious){
            if(ML_DECISION.MALICIOUS == makeDecision(training.getMaliciousProbabilityVector(),
                    training.getVanillaProbabilityVector(), maliciousSample)){
                totalCorrect += 1;
            }else {
                totalFalseNegatives += 1;
            }
        }

        testData.add((double)totalCorrect/totalNumberOfSamples);
        testData.add((double)totalFalsePositives/totalNumberOfSamples);
        testData.add((double)totalFalseNegatives/totalNumberOfSamples);

        return testData;
    }

    private void writeArrayToFile(String fileName, ArrayList<ArrayList<Double>> arrayListToWrite){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(arrayListToWrite);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void saveTrainingToFile(){
        writeArrayToFile(maliciousProbabilityVectorFile, training.getMaliciousProbabilityVector());
        writeArrayToFile(vanillaProbabilityVectorFile, training.getVanillaProbabilityVector());
    }

    private ArrayList<ArrayList<Double>> readTrainingFromFile(String fileName){
        ArrayList<ArrayList<Double>> arrayList;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            arrayList = (ArrayList<ArrayList<Double>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }

        return arrayList;
    }

    private boolean getTrainingData(){
        File maliciousFile = new File(maliciousProbabilityVectorFile);

        if(maliciousFile.exists() && !maliciousFile.isDirectory()) {
            savedMaliciousProbabilityVector = readTrainingFromFile(maliciousProbabilityVectorFile);

            if(null == savedMaliciousProbabilityVector){
                return false;
            }

        }else {
            return false;
        }

        File vanillaFile = new File(vanillaProbabilityVectorFile);

        if(vanillaFile.exists() && !vanillaFile.isDirectory()) {
            savedVanillaProbabilityVector = readTrainingFromFile(vanillaProbabilityVectorFile);

            if(null == savedVanillaProbabilityVector){
                return false;
            }

        }else {
            return false;
        }

        return true;
    }

     private void trainModel(){
        FeatureBuilder featureBuilder = new FeatureBuilder();
        featureBuilder.createFeatureVectors();

        training = new Training();
        training.train(featureBuilder.getTrainingMalicious(), featureBuilder.getTrainingVanilla());
        System.out.println("Finished training");

        if(testOutput){
            ArrayList<Double> testData = test(featureBuilder.getTestingMalicious(), featureBuilder.getTestingVanilla());
            System.out.println("Finished testing, success rate is: " + testData.get(0) + "\n" +
                               "False positive rate is: " + testData.get(1) + "\n" +
                               "False negative rate is: " + testData.get(2) + "\n");

        }

        saveTrainingToFile();
        savedVanillaProbabilityVector = training.getVanillaProbabilityVector();
        savedMaliciousProbabilityVector = training.getMaliciousProbabilityVector();
    }

    public void run() {
        ArrayList<Map<String, String>> logData = new ArrayList<>();
        FeatureBuilder featureBuilder = new FeatureBuilder();

        /* If there is no training data, then need to train the model to create the probability vectors */
        if (!getTrainingData()) {
            trainModel();
        }

        /* Start the main loop */
        while(true) {
            /* If no new data, sleep for a minute then ask again */
            if (0 == sync_to.getData(moduleName, logData, 20)) {
                try {
                    Thread.sleep(60000);
                    continue;
                } catch (InterruptedException e) {
                    return;
                }
            }

            for (Map<String, String> logLine : logData) {
                ArrayList<String> parsedLogData = logParser.parseLine(logLine);

                if(null == parsedLogData){
                    continue;
                }

                ArrayList<Integer> featureVector = featureBuilder.createFeatureVector(parsedLogData.get(0));

                if(ML_DECISION.BENIGN == makeDecision(savedMaliciousProbabilityVector, savedVanillaProbabilityVector,
                        featureVector)){
                    Module_Alert module_alert = new Module_Alert(moduleName, "12:30",MESSAGE_TEMPLATE, logLine);

                    sync_to.alert(module_alert);
                }
            }
        }
    }
}
