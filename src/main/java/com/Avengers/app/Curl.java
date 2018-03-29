package com.Avengers.app;

import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Curl {
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

    private static Date convertTimestampToDate(long timeStamp){
        return new Date(timeStamp * 1000);
    }

    public static void main( String[] args )
    {
        String ip = args[2];
        List<String> country_city = getCountryCityByIp(ip);

        if (country_city != null)
        {
            for (String aCountry_city : country_city) System.out.println(aCountry_city);
        }
        List<String> city_offset = getOffsetByCity("usa", "miami");
        if (city_offset != null)
        {
            for (String aCity_offset : city_offset) System.out.println(aCity_offset);
        }
        String timestamp = args[3];
        timestamp = timestamp.substring(1,11);

        Date d = convertTimestampToDate(Long.valueOf(timestamp));
        System.out.println(d);

    }
}