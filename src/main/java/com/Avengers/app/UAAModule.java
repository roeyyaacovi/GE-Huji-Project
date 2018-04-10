package com.Avengers.app;

import javafx.util.Pair;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UAAModule {
    private Map<String, ArrayList<my_date>> instance_map;



    public UAAModule()
    {
        instance_map = new HashMap<>();
    }

    public class my_date
    {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private double second;

        public my_date(int y, int m, int d, int h, int mi, double s)
        {
            year = y;
            month = m;
            day = d;
            hour = h;
            minute = mi;
            second = s;
        }
    }

    public boolean check_if_suspect(String line){
        String[] phrases = {"authentication failed"};
        for (String phrase: phrases){
            if (line.toLowerCase().contains(phrase.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public  String parse_line(String line)
    {
        String inst = "";
        String pattern = "\"time\":\"((\\d+)-(\\d+)-(\\d+))T((\\d+):(\\d+):(.*))\\+.*inst\":\"(.*)\",\"tid.*";
        Pattern r = Pattern.compile(pattern);
        line = "{\"time\":\"2017-12-10T11:34:18.388+0000\",\"tnt\":\"495bf861-b1f8-4de7-a8d6-b8a96f392337\",\"corr\":\"b0ac39f8ecb10b0f\",\"appn\":\"cf3-staging-uaa\",\"dpmt\":\"88a65cea-5d1a-4c9d-86e0-c3842093c4af\",\"inst\":\"25d195dd-6120-4b89-579a-a7a9921c50cf\",\"tid\":\"http-nio-8080-exec-5\",\"mod\":\"JwtBearerAssertionAuthenticationFilter.java\",\"lvl\":\"DEBUG\",\"msg\":\"jwt-bearer authentication failed. Unknown client.\"}";
        Matcher m = r.matcher(line);
        if (m.find( )) {
            inst = m.group(9);
            if (!instance_map.containsKey(inst)){
                instance_map.put(inst, new ArrayList<>());
            }
            instance_map.get(inst).add(new my_date(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Double.parseDouble(m.group(8))));
            return inst;
        }
        return inst;
    }

    public void start()
    {
        String line = "";
//        String line = wait_for_input();
        String res = "";
        if (check_if_suspect(line)){
            res = parse_line(line);
            if (!res.isEmpty())
            {

            }
        }



    }

    public static void main(String[] args)

    {
        UAAModule a = new UAAModule();
        a.parse_line("");
    }
}
