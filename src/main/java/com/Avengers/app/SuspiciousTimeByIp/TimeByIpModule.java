package com.Avengers.app.SuspiciousTimeByIp;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;
import com.Avengers.app.Security.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TimeByIpModule extends Interface_Module {

    /* Parser object that parses the log message */
    private Parser logParser = new Parser();

    public TimeByIpModule(String module_name, Framework_Module fm)
    {
        moduleName = module_name;
        sync_to = fm;
    }

    private static List<String> getCountryCityByIp(String ip) {
        {
            try {
                String adrr = "http://api.db-ip.com/v2/free/" + ip;
                URL url = new URL(adrr);
                try {
                    List<String> ret = new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        if(line.contains("countryName") || line.contains("city"))
                        {
                            if(line.contains("countryName") && line.contains("United States"))
                            {
                                ret.add("    \"countryName\": \"USA\",");
                                continue;
                            }

                            ret.add(line);
                        }
                    }
                    return ret;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static List<String> getOffsetByCity(String Country, String city) {
        {
            try {
                String adrr = "https://www.timeanddate.com/time/zone/" + Country + "/" + city;
                URL url = new URL(adrr);
                try {
                    List<String> ret = new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        if(line.contains("UTC/GMT"))
                        {
                            String msgBody = line.split("UTC/GMT")[1];
                            String hourOffset = msgBody.split("hours")[0];
                            ret.add(hourOffset);
                        }
                    }
                    return ret;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static Date convertTimestampToDate(long timeStamp){
        return new Date(timeStamp * 1000);
    }

    private static Boolean isTimeSuspicious(Date timeToCompare){
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date start_time = parser.parse("00:00");
            Date end_time = parser.parse("04:00");
            int timeToCompareHours = timeToCompare.getHours();
            int timeToCompareMinutes = timeToCompare.getMinutes();
            String timeToCompareString = String.valueOf(timeToCompareHours) + ":" + String.valueOf(timeToCompareMinutes);
            Date compare = parser.parse(timeToCompareString);
//            System.out.println(compare);
            if (compare.after(start_time) && compare.before(end_time)) {
                // if here - suspicious
                System.err.println("     " +"warning");
                return true;
            }
            else {
                System.out.println("     " +"fine");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void run() {
        ArrayList<Map<String, String>> logData = new ArrayList<>();

        while (true) {
            /* If no new data, sleep for a minute then ask again */
            if (0 == sync_to.getData(moduleName, logData, 100)) {
                try {
                    Thread.sleep(60000);
                    continue;
                } catch (InterruptedException e) {
                    return;
                }
            }
            for (Map<String, String> logLine : logData) {
                System.out.println(logLine.get("timestamp"));
//                String ip = logLine.get("ip");
                String ip = "52.11.117.32";
                List<String> country_city = getCountryCityByIp(ip);

                if (country_city != null) {
                    for (String aCountry_city : country_city) System.out.println(aCountry_city);
                }
//                List<String> city_offset = getOffsetByCity("china", "shanghai");
                List<String> city_offset = getOffsetByCity("china", "shanghai");
                int city_offset_int = 0;
                if (city_offset != null) {
                    String offset_to_parse = city_offset.get(0).substring(1, city_offset.get(0).length() - 1);
                    city_offset_int = Integer.parseInt(offset_to_parse);
                }
                String timestamp = "513091064211";
                timestamp = timestamp.substring(1, 11);
                Date d = convertTimestampToDate(Long.valueOf(timestamp));
                Date local_time_at_ip_origin = new Date(d.getTime() + 3600000 * city_offset_int);
                System.out.println("     " + local_time_at_ip_origin);

                if (isTimeSuspicious(local_time_at_ip_origin)) {
                    ArrayList<String> parsedLogData = logParser.parseLine(logLine);

                    if (null == parsedLogData) {
                        continue;
                    }
                    Module_Alert module_alert = new Module_Alert(parsedLogData.get(1),
                            "TimeByIp Module", logLine);
                    sync_to.alert("SuspiciousTimeByIp.SuspiciousTimeByIp", module_alert);
                    return;
                }
            }
        }

    }
}
