package com.Avengers.app.Security;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecurityModule {

    /* Full path to file that will hold a list of connections between domains */
    private static final String graphInputFile = "";

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
            logData = getMoreData();

            for(Map<String, String> logLine: logData){
                ArrayList<String> parsedLogData =   logParser.parseLine(logLine);

                handleLine(parsedLogData);

            }

        }

    }

    private int handleLine(ArrayList<String> parsedLogData){
        String domainName = parsedLogData.get(0);
        Node accessedNode = domainToNode.get(domainName);
        boolean flagRaised;



        //flagRaised = accessedNode.checkIfTimeSlotValid();



        return 0;
    }

    private ArrayList<Map<String, String>> getMoreData(){
        ArrayList<Map<String, String>> logData = new ArrayList<>();

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

            /* First node does not have any parents */
            if(i != 0){
                childNode.addParentNode(parentNode);
            }
        }
    }


}
