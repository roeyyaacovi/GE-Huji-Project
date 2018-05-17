package com.Avengers.app.Security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;

public class SecurityModule extends Interface_Module{
    /* Full path to file that will hold a list of connections between domains */
    private static final String graphInputFile = "C:\\Users\\roeyy\\Desktop\\School\\graph.txt";

    /* A mapping between the domain's name, and the Node that represents it */
    private Map<String, Node> domainToNode = new HashMap<String, Node>();

    /* Parser object that parses the log message */
    private Parser logParser = new Parser();

    /**
     * Constructor
     */
    public SecurityModule(String moduleName, Framework_Module frameworkModule)
    {
        this.moduleName = moduleName;
        this.sync_to = frameworkModule;
    }

    /**
     * Read input file that has the dependencies between each server. It is built so that each row has a
     * pair. The right domain is the domain that should only be accessed through the left domain.
     */
    private boolean buildGraph(){
        BufferedReader br;
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

    private int init(){
        if(!buildGraph()){
            return -1;
        }

        return 0;
    }

    /**
     * Starts the main loop of the module.
     */
    public void run(){
        ArrayList<Map<String, String>> logData = new ArrayList<>();

        if(-1 == init()){
            return;
        }

        while(true){
            /* If no new data, sleep for a minute then ask again */
            if( 0 == sync_to.getData(moduleName, logData, 20)){
                try{
                    Thread.sleep(60000);
                    continue;
                }catch (InterruptedException e){
                    return;
                }
            }

            for(Map<String, String> logLine: logData){
                ArrayList<String> parsedLogData = logParser.parseLine(logLine);

                if(null == parsedLogData){
                    continue;
                }

                if(!checkLine(parsedLogData)){
                    /* Raise flag to framework */
                    Module_Alert module_alert = new Module_Alert(parsedLogData.get(1),
                            "Security Module", logLine);

                    sync_to.alert("Security.SecurityModule", module_alert);
                }

            }
        }

    }

    private boolean checkLine(ArrayList<String> parsedLogData){
        String domainName = parsedLogData.get(0);

        /* If the key is not in the map, then we don't care and just return that its ok */
        Node accessedNode = domainToNode.get(domainName);

        if(null == accessedNode){
            return true;
        }

        return accessedNode.isNewRequestValid(parsedLogData.get(1), parsedLogData.get(2));
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
