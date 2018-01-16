package com.Avengers.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {


    public static ArrayList<ArrayList<String>> getAttributeLogMessage(String msg) {
        ArrayList<ArrayList<String>> line = new ArrayList<>();
        String[] msgAfterSplit = msg.split("message_type");
        String msgBody = msgAfterSplit[0].split("message:")[1];
        String afterMsgType = "message_type" + msgAfterSplit[1];
        msgAfterSplit = afterMsgType.split("\\s");
        for (String splitBySpace : msgAfterSplit) {
            ArrayList<String> pair = new ArrayList<>();
            if (splitBySpace.contains(":") && !splitBySpace.equals(":")) {
                String[] splitByTowDots = splitBySpace.split(":");
                if (splitByTowDots[0].equals("timestamp")) {
                    pair.add("msgtimestamp");
                } else {
                    pair.add(splitByTowDots[0]);
                }
                pair.add(splitByTowDots[1]);
                //   int att_inx = attributes.indexOf(splitByTowDots[0]);
                line.add(pair);
                //   values.set(att_inx, values.get(attributes.indexOf(splitByTowDots[0])) + 1);
//                        values.indexOf(values.get(attributes.indexOf(splitByTowDots[0])))
            }
        }
        ArrayList<String> msgPair = new ArrayList<>();
        msgPair.add("message");
        msgPair.add(msgBody);
        line.add(msgPair);
        return line;

    }

    public static ArrayList<ArrayList<ArrayList<String>>> getAttributesSet(BufferedReader br) {
        ArrayList<ArrayList<ArrayList<String>>> allInfo = new ArrayList<>();
        List<Integer> values = new ArrayList<Integer>();
        int pos = 0;
        try {

            String sCurrentLine;
            int lineNumber = 0;
            while (pos < 500 && (sCurrentLine = br.readLine()) != null) {
//                boolean isOpen = false;
                if (lineNumber == 0 || lineNumber == 1) {
                    lineNumber++;
                    continue;
                }
                ArrayList<ArrayList<String>> line = new ArrayList<>();
                String[] splitBySpaces = sCurrentLine.split("\\s");
                for (String splitBySpace : splitBySpaces) {
                    ArrayList<String> pair = new ArrayList<>();
                    if (splitBySpace.contains(":") && !splitBySpace.equals(":")) {
                        String[] splitByTowDots = splitBySpace.split(":");
                        if (splitByTowDots[0].equals("logMessage")) // this is the last attribute
                        {
                            String splitByLogMessage = sCurrentLine.split("logMessage:")[1];
                            line.addAll(getAttributeLogMessage(splitByLogMessage));
                            break;
                        } else {
                            pair.add(splitByTowDots[0]);
                            pair.add(splitByTowDots[1]);
                        }
                        //   int att_inx = attributes.indexOf(splitByTowDots[0]);
                        line.add(pair);
                        //   values.set(att_inx, values.get(attributes.indexOf(splitByTowDots[0])) + 1);
//                        values.indexOf(values.get(attributes.indexOf(splitByTowDots[0])))
                    }
                }
                lineNumber++;
                allInfo.add(line);
                pos++;
            }
//            for(String stt : attributes)
//            {
//                System.out.println(stt);
//            }
        } catch (IOException e) {

            e.printStackTrace();

        }
        return allInfo;
    }
}

//    public static List getValues() {
//        BufferedReader br = null;
//        FileReader fr = null;
//        List<String> values = new ArrayList<String>();
//        try {
//            //br = new BufferedReader(new FileReader(FILENAME));
//            fr = new FileReader(FILENAME);
//            br = new BufferedReader(fr);
//
//            String sCurrentLine;
//            int lineNumber = 0;
//            while ((sCurrentLine = br.readLine()) != null) {
//                if (lineNumber == 0 || lineNumber == 1) {
//                    lineNumber++;
//                    continue;
//                }
//                String[] splitBySpaces = sCurrentLine.split("\\s");
//                for (String splitBySpace : splitBySpaces) {
//                    if (splitBySpace.contains(":") && !splitBySpace.equals(":")) {
//                        String[] splitByTowDots = splitBySpace.split(":");
//
//                        if(!values.contains(splitByTowDots[0]))
//                        {
//                            values.add(splitByTowDots[0]);
//                        }
//                    }
//                }
//                lineNumber++;
//            }
////            for(String stt : attributes)
////            {
////                System.out.println(stt);
////            }
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        } finally {
//            try {
//                if (br != null)
//                    br.close();
//                if (fr != null)
//                    fr.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return attributes;
//    }

