package com.Avengers.app.Security;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childrenNodes;
    private ArrayList<Node> parentNodes;
    private ArrayList<TimeSlot> activeTimeSlots;
    private String domainName;

    public Node(String domainName){
        this.childrenNodes = new ArrayList<>();
        this.parentNodes = new ArrayList<>();
        this.domainName = domainName;
    }

    public void addChildNode(Node childNode){
        if(null == childNode){
            return;
        }

        childrenNodes.add(childNode);
    }

    public String getDomainName(){
        return domainName;
    }

    public void addParentNode(Node parentNode){
        if(null == parentNode){
            return;
        }

        parentNodes.add(parentNode);
    }

    public boolean newRequest(TimeSlot newTimeSlot){
        /* Check if any of its parents have valid time slots */
        for(Node parentNode : parentNodes){
            if(checkIfTimeSlotValid(newTimeSlot)){
                activeTimeSlots.add(newTimeSlot);
                return true;
            }
        }

        return false;
    }

    public boolean newRequest(int startTime, int responseTime){
        TimeSlot newTimeSlot = new TimeSlot(startTime, responseTime);

        return newRequest(newTimeSlot);
    }

    public boolean checkIfTimeSlotValid(TimeSlot childTimeSlot){
        for(TimeSlot timeSlot : activeTimeSlots){
            if(timeSlot.isInsideTimeSlot(childTimeSlot)){
                return true;
            }
        }

        return false;
    }
}
