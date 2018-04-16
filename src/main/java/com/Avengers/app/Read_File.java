package com.Avengers.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Read_File {


    public static Map<String, String> getAttributeLogMessage(String msg) {
        Map<String, String> line = null;
        String[] msgAfterSplit = msg.split("message_type");
        String msgBody = msgAfterSplit[0].split("message:")[1];
        String afterMsgType = "message_type" + msgAfterSplit[1];
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
        Map<String, String> allInfo = null;
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
                    allInfo.put(splitByTowDots[0], splitByTowDots[1]);
                }
            }
        }
        return allInfo;
    }
}


