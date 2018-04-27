package com.Avengers.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feature_Extraction extends Interface_Module {
    public Feature_Extraction(String m, Framework_Module fm){
        moduleName = m;
        sync_to = fm;
    }


    private double sampleAvgMsgLength(ArrayList<Map<String, String>> buff, int number_of_lines)
    {
        int length_sum = 0;
        for (Map<String, String> line : buff){
            length_sum += line.get("message").length();
        }
        return length_sum / number_of_lines;
    }

    private Map.Entry<String, Integer> maxRequests(ArrayList<Map<String, String>> buff, int number_of_lines)
    {
        Map<String, Integer> all_app_ids = new HashMap<>();
        for (Map<String, String> line : buff){
            String app_id_to_check = line.get("app_id");
            if(all_app_ids.containsKey(app_id_to_check))
            {
                all_app_ids.put(app_id_to_check, all_app_ids.get(app_id_to_check)+1);
            }
            else
            {
                all_app_ids.put(app_id_to_check, 1);
            }
        }
        int maxval = 0;
        Map.Entry<String, Integer> retEntry = null;
        for (Map.Entry<String, Integer> entry : all_app_ids.entrySet())
        {
            if (entry.getValue() > maxval)
            {
                retEntry = entry;
                maxval = entry.getValue();
            }
        }
        return retEntry;
    }

    public void printAppIds(ArrayList<Map<String, String>> buff)
    {
        for (Map<String, String> line : buff){
            String app_id_to_check = line.get("app_id");
            System.out.println(app_id_to_check);
        }
        System.out.println();
    }

    public void maxAppId(ArrayList<Map<String, Integer>> sampleMaxRequestsAppIds, Map.Entry<String, Integer>
            requestsParser, Map<String, Double> features_vector)
    {
        boolean appIdExists = false;
        for(Map<String, Integer> entry : sampleMaxRequestsAppIds)
        {
            if(entry.containsKey(requestsParser.getKey())) // the appId is new - assign new number
            {
                features_vector.put("sampleMaxRequestsAppId", entry.get(requestsParser.getKey())+0.0);
                appIdExists = true;
                break;
            }
        }
        if (!appIdExists)
        {
            Map<String, Integer> sampleMaxRequestsAppId = new HashMap<>(); // map between appId and a number
            sampleMaxRequestsAppId.put(requestsParser.getKey(), sampleMaxRequestsAppIds.size()+1);
            sampleMaxRequestsAppIds.add(sampleMaxRequestsAppId);
            features_vector.put("sampleMaxRequestsAppId", sampleMaxRequestsAppIds.size()+0.0);

        }
    }

    @Override

    public void run() {
        int sample_size = 10;
        ArrayList<Map<String, Double>> allFeaturesVector = new ArrayList<>();
        ArrayList<Map<String, Integer>> sampleMaxRequestsAppIds = new ArrayList<>();
        int i = 0;
        while (i<5) //start sample
        {
            ArrayList<Map<String, String>> buff = new ArrayList<>();

            int number_of_lines = sync_to.getData("Feature_Extraction", buff, sample_size);
            if(number_of_lines == 0)
            {
                return;
            }

            Map<String, Double> features_vector = new HashMap<>();
            double avgLength = sampleAvgMsgLength(buff, number_of_lines);
            Map.Entry<String, Integer> requestsParser = maxRequests(buff, number_of_lines);
            features_vector.put("msgLength", avgLength);
            features_vector.put("sampleMaxRequests", requestsParser.getValue().doubleValue());
            maxAppId(sampleMaxRequestsAppIds, requestsParser, features_vector);
            allFeaturesVector.add(features_vector);

            i++;
        }
        System.out.println(allFeaturesVector);
//        ArrayList<Map<String, String>> buff2 = new ArrayList<>();
//        ArrayList<Map<String, String>> buff3 = new ArrayList<>();
//
//        sync_to.getData("Feature_Extraction", buff2, sample_size);
//        sync_to.getData("Feature_Extraction", buff3, sample_size);
//
//        ArrayList<Map<String, String>> buff4 = new ArrayList<>();
//        ArrayList<Map<String, String>> buff5 = new ArrayList<>();
//
//        sync_to.getData("Feature_Extraction", buff4, sample_size);
//        sync_to.getData("Feature_Extraction", buff5, sample_size);
////        printAppIds(buff);
//        printAppIds(buff5);
//
//        Map<String, Double> features_vector2 = new HashMap<>();
//        double avgLength2 = sampleAvgMsgLength(buff5, number_of_lines);
//        Map.Entry<String, Integer> requestsParser2 = maxRequests(buff5, number_of_lines);
//        features_vector2.put("msgLength", avgLength2);
//        features_vector2.put("sampleMaxRequests", requestsParser2.getValue().doubleValue());
//        maxAppId(sampleMaxRequestsAppIds, requestsParser2, features_vector2);
//        System.out.println(features_vector2);

        //end sample
    }
}
