package com.Avengers.app.Security;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SecurityModule {
    private static final String graphInputFile = "";
    private Map<String, Node> domainToNode = new HashMap<String, Node>();


    /**
     * Read input file that has the dependencies between each server. It is built so that each row has a
     * pair. The right domain is the domain that should only be accessed through the left domain.
     */
    private void buildGraph(){
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
        }
    }

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
