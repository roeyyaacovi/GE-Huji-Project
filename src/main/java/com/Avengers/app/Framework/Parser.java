package com.Avengers.app.Framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {


    public static Map<String, String> getAttributeLogMessage(String msg) {
        Map<String, String> line = new HashMap<>();
        String[] msgAfterSplit = msg.split("message_type");
        String msgBody = msgAfterSplit[0].split("message:")[1];
        String afterMsgType = "message_type";
        if (msgAfterSplit.length > 1) {
            afterMsgType += msgAfterSplit[1];
        }
        msgAfterSplit = afterMsgType.split("\\s");
        for (String splitBySpace : msgAfterSplit) {
            if (splitBySpace.contains(":") && !splitBySpace.equals(":")) {
                String[] splitByTowDots = splitBySpace.split(":");
                if (splitByTowDots[0].equals("timestamp")) {
                    line.put("msgtimestamp", splitByTowDots[1]);
                } else {
                    line.put(splitByTowDots[0], splitByTowDots[1]);
                }
            }
        }
        line.put("message", msgBody);
        return line;

    }

    public static Map<String, String> getAttributesSet(String sCurrentLine) {
        Map<String, String> allInfo = new HashMap<>();
        String[] splitBySpaces = sCurrentLine.split("\\s");
        for (String splitBySpace : splitBySpaces) {
            if (splitBySpace.contains(":") && !splitBySpace.equals(":")) {
                String[] splitByTowDots = splitBySpace.split(":");
                if (splitByTowDots[0].equals("logMessage")) // this is the last attribute
                {
                    String splitByLogMessage = sCurrentLine.split("logMessage:")[1];
                    allInfo.putAll(getAttributeLogMessage(splitByLogMessage));
                    break;
                } else {
                    if (splitByTowDots.length > 1)
                        allInfo.put(splitByTowDots[0], splitByTowDots[1]);
                }
            }
        }
        return allInfo;
    }
}


