package com.Avengers.app.SQLI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class FeatureBuilder {
    /* Files containing SQL statements */
    private static final String maliciousSqlFile = "malicious_sql.txt";
    private static final String vanillaSqlFile = "vanilla_sql.txt";

    /* Tokens */
    private static final String[] SINGLE_LINE_COMMENT = {"--"};
    private static final String[] MULTI_LINE_COMMENT = {"/**/"};
    private static final String[] OPERATOR = {"<", ">", "<=", ">=", "=", "==", "!=", "<<", ">>", "|", "&", "-", "+", "%", "^"};
    private static final String[] LOGICAL_OPERATOR = {"NOT", "AND", "OR", "&&", "||"};
    private static final String[] PUNCTUATION = {"[", "]", "(", ")", ",", ";", "'", "\""};
    private static final String[] KEYWORDS = {"SELECT", "UPDATE", "INSERT", "CREATE", "DROP", "ALTER", "RENAME"};
    static final String[][] TOKEN_LIST = {SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, OPERATOR,
            LOGICAL_OPERATOR, PUNCTUATION, KEYWORDS};

    private List<ArrayList<Integer>> trainingMalicious;
    private List<ArrayList<Integer>> trainingVanilla;
    private List<ArrayList<Integer>> testingMalicious;
    private List<ArrayList<Integer>> testingVanilla;

    private static final double TRAINING_RATIO = 0.8;

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

    ArrayList<Integer> createFeatureVector(String sqlString){
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
        String sqlString;

        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));

            while((sqlString = bufferedReader.readLine()) != null) {
                featureVectors.add(createFeatureVector(sqlString));
            }

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return featureVectors;
    }

    void createFeatureVectors(){
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
                (int)Math.floor(TRAINING_RATIO*vanillaFeatureVectors.size()), vanillaFeatureVectors.size());
    }

    List<ArrayList<Integer>> getTrainingMalicious() {
        return trainingMalicious;
    }

    List<ArrayList<Integer>> getTrainingVanilla() {
        return trainingVanilla;
    }

    List<ArrayList<Integer>> getTestingMalicious() {
        return testingMalicious;
    }

    List<ArrayList<Integer>> getTestingVanilla() {
        return testingVanilla;
    }
}
