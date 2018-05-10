package com.Avengers.app.Security;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childrenNodes;
    private ArrayList<Node> parentNodes;
    private ArrayList<TimeSlot> activeTimeSlots = new ArrayList<>();
    private String domainName;

    Node(String domainName){
        this.childrenNodes = new ArrayList<>();
        this.parentNodes = new ArrayList<>();
        this.domainName = domainName;
    }

    void addChildNode(Node childNode){
        if(null == childNode){
            return;
        }

        childrenNodes.add(childNode);
    }

    public String getDomainName(){
        return domainName;
    }

    public ArrayList<Node> getParentNodes(){
        return parentNodes;
    }

    void addParentNode(Node parentNode){
        if(null == parentNode){
            return;
        }

        parentNodes.add(parentNode);
    }

    private boolean isNewRequestValid(TimeSlot newTimeSlot){
        /* If no parents, then the request is definitely valid */
        if(parentNodes.isEmpty()){
            activeTimeSlots.add(newTimeSlot);
            return true;
        }

        /* If any of the parents have a valid time slot, return true */
        for(Node parentNode : parentNodes){
            if(parentNode.checkIfTimeSlotValid(newTimeSlot)){
                activeTimeSlots.add(newTimeSlot);
                return true;
            }
        }

        return false;
    }

    boolean isNewRequestValid(String startTime, String responseTime){
        TimeSlot newTimeSlot = new TimeSlot(startTime, responseTime);

        return isNewRequestValid(newTimeSlot);
    }

    private boolean checkIfTimeSlotValid(TimeSlot childTimeSlot){
        for(TimeSlot timeSlot : activeTimeSlots){
            if(timeSlot.isInsideTimeSlot(childTimeSlot)){
                return true;
            }
        }

        return false;
    }
}
