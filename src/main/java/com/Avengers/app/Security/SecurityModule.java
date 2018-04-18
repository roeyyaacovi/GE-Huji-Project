package com.Avengers.app.Security;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecurityModule {
    /* For testing */
    private static final String log1 = "message:\"A-cf3.run.aws-usw01-dev.ice.predix.io - [2017-12-10T11:34:18.316+0000] \\\"GET /cs/v1/certificate/default-predix-performance/620035de-4262-496f-b8eb-05f4f9083482/publickey HTTP/1.1\\\" 200 0 592 \\\"-\\\" \\\"Apache-HttpClient/4.5.2 (Java/1.8.0_152)\\\" \\\"10.72.131.25:52896\\\" \\\"10.72.134.237:63507\\\" x_forwarded_for:\\\"-\\\" x_forwarded_proto:\\\"http\\\" vcap_request_id:\\\"476623cf-7089-4d5d-69ae-8dc26e107c31\\\" response_time:0.003020452 app_id:\\\"7ed06b66-7a25-4ed3-8731-a944ea71751b\\\" app_index:\\\"-\\\" x_b3_traceid:\\\"deab9b2b835baac9\\\" x_b3_spanid:\\\"deab9b2b835baac9\\\" x_b3_parentspanid:\\\"-\\\"\\n\" message_type:OUT timestamp:1512905658320085026 app_id:\"7ed06b66-7a25-4ed3-8731-a944ea71751b\" source_type:\"RTR\" source_instance:\"4\"";

    private static final String log2 = "message:\"B-cf3.run.aws-usw02-dev.ice.predix.io - [2017-12-10T11:34:18.316+0000] \\\"GET /cs/v1/certificate/default-predix-performance/620035de-4262-496f-b8eb-05f4f9083482/publickey HTTP/1.1\\\" 200 0 592 \\\"-\\\" \\\"Apache-HttpClient/4.5.2 (Java/1.8.0_152)\\\" \\\"10.72.131.25:52896\\\" \\\"10.72.134.237:63507\\\" x_forwarded_for:\\\"-\\\" x_forwarded_proto:\\\"http\\\" vcap_request_id:\\\"476623cf-7089-4d5d-69ae-8dc26e107c31\\\" response_time:0.003320452 app_id:\\\"7ed06b66-7a25-4ed3-8731-a944ea71751b\\\" app_index:\\\"-\\\" x_b3_traceid:\\\"deab9b2b835baac9\\\" x_b3_spanid:\\\"deab9b2b835baac9\\\" x_b3_parentspanid:\\\"-\\\"\\n\" message_type:OUT timestamp:1512905658320085026 app_id:\"7ed06b66-7a25-4ed3-8731-a944ea71751b\" source_type:\"RTR\" source_instance:\"4\"";

    private static final String log3 = "message:\"C-cf3.run.aws-usw03-dev.ice.predix.io - [2017-12-10T11:34:18.316+0000] \\\"GET /cs/v1/certificate/default-predix-performance/620035de-4262-496f-b8eb-05f4f9083482/publickey HTTP/1.1\\\" 200 0 592 \\\"-\\\" \\\"Apache-HttpClient/4.5.2 (Java/1.8.0_152)\\\" \\\"10.72.131.25:52896\\\" \\\"10.72.134.237:63507\\\" x_forwarded_for:\\\"-\\\" x_forwarded_proto:\\\"http\\\" vcap_request_id:\\\"476623cf-7089-4d5d-69ae-8dc26e107c31\\\" response_time:0.003320452 app_id:\\\"7ed06b66-7a25-4ed3-8731-a944ea71751b\\\" app_index:\\\"-\\\" x_b3_traceid:\\\"deab9b2b835baac9\\\" x_b3_spanid:\\\"deab9b2b835baac9\\\" x_b3_parentspanid:\\\"-\\\"\\n\" message_type:OUT timestamp:1512905658320085026 app_id:\"7ed06b66-7a25-4ed3-8731-a944ea71751b\" source_type:\"RTR\" source_instance:\"4\"";


    /* Full path to file that will hold a list of connections between domains */
    private static final String graphInputFile = "C:\\Users\\roeyy\\Desktop\\School\\GE-Huji-Project\\graph.txt";

     /* A mapping between the domain's name, and the Node that represents it */
    private Map<String, Node> domainToNode = new HashMap<String, Node>();

    /* Parser object that parses the log message */
    private Parser logParser = new Parser();

    /**
     * Read input file that has the dependencies between each server. It is built so that each row has a
     * pair. The right domain is the domain that should only be accessed through the left domain.
     */
    private boolean buildGraph(){
        BufferedReader br = null;
        String sCurrentLine;

        try {
            br = new BufferedReader(new FileReader(graphInputFile));

            while((sCurrentLine = br.readLine()) != null){
                String[] domains = sCurrentLine.split(",");

                if(domains.length < 2){
                    continue;
                }

                addNodesToGraph(domains);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int init(){
        if(!buildGraph()){
            return -1;
        }

        return 0;
    }

    /**
     * Starts the main loop of the module.
     * @return 0 if successfully started
     */
    public int start(){
        ArrayList<Map<String, String>> logData;


        while(true){
            //logData = getMoreData();
            logData = securityTester();

            for(Map<String, String> logLine: logData){
                ArrayList<String> parsedLogData =   logParser.parseLine(logLine);

                if(!checkLine(parsedLogData)){
                    /* Raise flag to framework */
                    System.out.println("Flag raised \n");
                }

            }
        }

    }

    private ArrayList<Map<String, String>> securityTester(){
        ArrayList<Map<String, String>> logData = new ArrayList<>();

        Map<String, String> map_1 = new HashMap<String, String>();

        map_1.put("origin", "gorouter");
        map_1.put("logMessage", log1);

        logData.add(map_1);

        Map<String, String> map_2 = new HashMap<String, String>();

        map_2.put("origin", "gorouter");
        map_2.put("logMessage", log2);

        logData.add(map_2);

        Map<String, String> map_3 = new HashMap<String, String>();

        map_3.put("origin", "gorouter");
        map_3.put("logMessage", log3);

        logData.add(map_3);

        return logData;

    }

    private boolean checkLine(ArrayList<String> parsedLogData){
        String domainName = parsedLogData.get(0);
        Node accessedNode = domainToNode.get(domainName);


        return accessedNode.isNewRequestValid(parsedLogData.get(1), parsedLogData.get(2));
    }

    private ArrayList<Map<String, String>> getMoreData(){
        ArrayList<Map<String, String>> logData = new ArrayList<>();

        /* Get data from framework */

        return logData;
    }

    /**
     * Add nodes to the graph. Built so that can handle more then just pairs. So for example if there is a line
     * that has "A,B,C" then A is parent of B, who is parent of C.
     * @param domains List of domains
     */
    private void addNodesToGraph(String[] domains){
        for(int i=0; i < domains.length-1; i++){
            Node parentNode = domainToNode.get(domains[i]);

            if(parentNode == null){
                parentNode = new Node(domains[i]);
                domainToNode.put(domains[i], parentNode);
            }

            Node childNode = domainToNode.get(domains[i + 1]);

            if (childNode == null) {
                childNode = new Node(domains[i + 1]);
                domainToNode.put(domains[i + 1], childNode);
            }

            parentNode.addChildNode(childNode);
            childNode.addParentNode(parentNode);

        }
    }


}
