package com.Avengers.app;//package com.Avengers.app;


import com.Avengers.app.Framework_Module;
import com.Avengers.app.Interface_Module;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.lang.Object;
//import org.apache.commons;

public class Curl extends Interface_Module {
    Map<String, Integer> hours = new HashMap<String, Integer>();
    String ip;
    public Curl(String a, Framework_Module fm)
    {
        moduleName = a;
        sync_to = fm;

        ArrayList<Map<String, String>> arrMap = new ArrayList<Map<String, String>>();
        sync_to.getData("TestClass", arrMap, 5);
        System.out.println(arrMap.get(4).get("ip"));
        ip = arrMap.get(4).get("ip");

    }

    public void run() {
        System.out.println("hola from Curl");
//        sync_to.getData()
        String[] args = new String[] {"snur", "is", "212.179.203.234", "51290565830681278"};
//        String ip = args[2];
        List<String> country_city = getCountryCityByIp(ip);

        if (country_city != null) {
            for (String aCountry_city : country_city) System.out.println(aCountry_city);
        }
        List<String> city_offset = getOffsetByCity("china", "shanghai");
        int city_offset_int = 0;
        if (city_offset != null) {
//            for (String aCity_offset : city_offset) System.out.println(aCity_offset);
            String offset_to_parse = city_offset.get(0).substring(1,city_offset.get(0).length()-1);
            city_offset_int = Integer.parseInt(offset_to_parse);
//            System.out.println(city_offset_int);
        }
        String timestamp = args[3];
        timestamp = timestamp.substring(1,11);
        Date d = convertTimestampToDate(Long.valueOf(timestamp));
        Date local_time_at_ip_origin = new Date(d.getTime() + 3600000 * city_offset_int);
//        System.out.println(d.getMinutes());
        System.out.println(local_time_at_ip_origin);
        if(isTimeSuspicious(local_time_at_ip_origin))
        {
            sync_to.alert(moduleName);
        }
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
                                ret.add("usa");
                                continue;
                            }

                            ret.add(line);
                        }
//                        System.out.println(line);
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
//                        System.out.println(line);
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

    private static int getMessageLength(String msg){
        // will get only the pure message
        return msg.length();
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
                System.out.println("warning");
                return true;
            }
            else {
                System.out.println("fine");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main( String[] args) {

        String ip = args[2];
        List<String> country_city = getCountryCityByIp(ip);

        if (country_city != null) {
            for (String aCountry_city : country_city) System.out.println(aCountry_city);
        }
        List<String> city_offset = getOffsetByCity("china", "shanghai");
        int city_offset_int = 0;
        if (city_offset != null) {
//            for (String aCity_offset : city_offset) System.out.println(aCity_offset);
            String offset_to_parse = city_offset.get(0).substring(1,city_offset.get(0).length()-1);
            city_offset_int = Integer.parseInt(offset_to_parse);
//            System.out.println(city_offset_int);
        }
        String timestamp = args[3];
        timestamp = timestamp.substring(1,11);
        Date d = convertTimestampToDate(Long.valueOf(timestamp));
        Date local_time_at_ip_origin = new Date(d.getTime() + 3600000 * city_offset_int);
//        System.out.println(d.getMinutes());
        System.out.println(local_time_at_ip_origin);
        isTimeSuspicious(local_time_at_ip_origin);
    }
}