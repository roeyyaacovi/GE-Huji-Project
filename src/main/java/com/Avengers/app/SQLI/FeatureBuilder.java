package com.Avengers.app.SQLI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class FeatureBuilder {
    private static final String maliciousSqlFile = "C:\\Users\\roeyy\\PycharmProjects\\sql\\malicious_sql";
    private static final String vanillaSqlFile = "C:\\Users\\roeyy\\PycharmProjects\\sql\\vanilla_sql";

    /* Tokens */
    private static final String[] SINGLE_LINE_COMMENT = {"--"};
    private static final String[] MULTI_LINE_COMMENT = {"/**/"};
    private static final String[] OPERATOR = {"<", ">", "<=", ">=", "=", "==", "!=", "<<", ">>", "|", "&", "-", "+", "%", "^"};
    private static final String[] LOGICAL_OPERATOR = {"NOT", "AND", "OR", "&&", "||"};
    private static final String[] PUNCTUATION = {"[", "]", "(", ")", ",", ";", "'", "\""};
    private static final String[] KEYWORDS = {"SELECT", "UPDATE", "INSERT", "CREATE", "DROP", "ALTER", "RENAME"};
    public static final String[][] TOKEN_LIST = {SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, OPERATOR,
            LOGICAL_OPERATOR, PUNCTUATION, KEYWORDS};

    private List<ArrayList<Integer>> trainingMalicious;
    private List<ArrayList<Integer>> trainingVanilla;
    private List<ArrayList<Integer>> testingMalicious;
    private List<ArrayList<Integer>> testingVanilla;

    private final double TRAINING_RATIO = 0.8;

    private String generateRandomString() {
        String stringChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < 8) {
            int index = (int) (rnd.nextFloat() * stringChars.length());
            salt.append(stringChars.charAt(index));
        }

        return salt.toString();
    }

    private int subStringCount(String str, String subStr){
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(subStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += subStr.length();
            }
        }

        return count;
    }

    public ArrayList<Integer> createFeatureVector(String sqlString){
        ArrayList<Integer> featureVector = new ArrayList<>();

        for(String[] tokens: TOKEN_LIST){
            Integer tokenCount = 0;

            for(String token: tokens){
                tokenCount += subStringCount(sqlString, token);
            }

            featureVector.add(tokenCount);
        }

        return featureVector;
    }

    private ArrayList<ArrayList<Integer>> createFeatureVectorsPerFile(String fileName){
        ArrayList<ArrayList<Integer>> featureVectors = new ArrayList<>();
        BufferedReader bufferedReader;
        String sCurrentLine;

        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));

            while((sCurrentLine = bufferedReader.readLine()) != null) {
                featureVectors.add(createFeatureVector(sCurrentLine));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return featureVectors;
    }

    public void createFeatureVectors(){
        ArrayList<ArrayList<Integer>> maliciousFeatureVectors = createFeatureVectorsPerFile(maliciousSqlFile);

        if(null == maliciousFeatureVectors){
            return;
        }

        ArrayList<ArrayList<Integer>> vanillaFeatureVectors = createFeatureVectorsPerFile(vanillaSqlFile);

        if(null == vanillaFeatureVectors){
            return;
        }


        trainingMalicious = maliciousFeatureVectors.subList(0,
                (int)Math.floor(TRAINING_RATIO*maliciousFeatureVectors.size()));

        trainingVanilla = vanillaFeatureVectors.subList(0,
                (int)Math.floor(TRAINING_RATIO*vanillaFeatureVectors.size()));

        testingMalicious = maliciousFeatureVectors.subList(
                (int)Math.floor(TRAINING_RATIO*maliciousFeatureVectors.size()), maliciousFeatureVectors.size());

        testingVanilla = vanillaFeatureVectors.subList(
                (int)Math.floor(TRAINING_RATIO*maliciousFeatureVectors.size()), maliciousFeatureVectors.size());
    }

    public List<ArrayList<Integer>> getTrainingMalicious() {
        return trainingMalicious;
    }

    public List<ArrayList<Integer>> getTrainingVanilla() {
        return trainingVanilla;
    }

    public List<ArrayList<Integer>> getTestingMalicious() {
        return testingMalicious;
    }

    public List<ArrayList<Integer>> getTestingVanilla() {
        return testingVanilla;
    }
}
