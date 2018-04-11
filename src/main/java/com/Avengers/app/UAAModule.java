package com.Avengers.app;

import javafx.util.Pair;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UAAModule {
    private Map<String, ArrayList<my_date>> instance_map;
    private int LOGS_PER_MINUTE = 5;
    private int NUM_OF_MINUTES_TO_CHECK = 0;



    public UAAModule()
    {
        instance_map = new HashMap<>();
    }

    public class my_date {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private double second;
        private int BIGGER = -1;
        private int EQUAL = 0;
        private int SMALLER = 1;

        my_date(){}

        my_date(int y, int m, int d, int h, int mi, double s) {
            year = y;
            month = m;
            day = d;
            hour = h;
            minute = mi;
            second = s;
        }

        public int compare(my_date other) {
            if (other.year == this.year) {
                if (other.month == this.month) {
                    if (other.day == this.day) {
                        if (other.hour == this.hour) {
                            if (other.minute == this.minute) {
                                if (other.second == this.second)
                                    return EQUAL;
                                else
                                    return Double.compare(this.second, other.second);
                            } else
                                return other.minute - this.minute;
                        } else
                            return other.hour - this.hour;
                    } else
                        return other.day - this.day;
                } else
                    return other.month - this.month;
            } else
                return other.year - this.year;
        }

        public boolean same_minute(my_date other)
        {
            return other.year == this.year && other.month == this.month && other.day == this.day &&
                    other.hour == this.hour && (other.minute - this.minute <= NUM_OF_MINUTES_TO_CHECK);
        }

        private boolean same_hour(my_date other)
        {
            return other.year == this.year && other.month == this.month && other.day == this.day &&
                    other.hour == this.hour;
        }
    }

    private boolean check_if_suspect(String line){
        String[] phrases = {"authentication failed"};
        for (String phrase: phrases){
            if (line.toLowerCase().contains(phrase.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private  Pair<String, my_date> parse_line(String line)
    {
        String inst = "";
        my_date d = new my_date();
        String pattern = "\"time\":\"((\\d+)-(\\d+)-(\\d+))T((\\d+):(\\d+):(.*))\\+.*inst\":\"(.*)\",\"tid.*";
        Pattern r = Pattern.compile(pattern);
        line = "{\"time\":\"2017-12-10T11:34:18.388+0000\",\"tnt\":\"495bf861-b1f8-4de7-a8d6-b8a96f392337\",\"corr\":\"b0ac39f8ecb10b0f\",\"appn\":\"cf3-staging-uaa\",\"dpmt\":\"88a65cea-5d1a-4c9d-86e0-c3842093c4af\",\"inst\":\"25d195dd-6120-4b89-579a-a7a9921c50cf\",\"tid\":\"http-nio-8080-exec-5\",\"mod\":\"JwtBearerAssertionAuthenticationFilter.java\",\"lvl\":\"DEBUG\",\"msg\":\"jwt-bearer authentication failed. Unknown client.\"}";
        Matcher m = r.matcher(line);
        if (m.find( )) {
            inst = m.group(9);
            if (!instance_map.containsKey(inst)){
                instance_map.put(inst, new ArrayList<>());
            }
            d = new my_date(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)),
                    Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Double.parseDouble(m.group(8)));
            instance_map.get(inst).add(d);
        }
        return new Pair<>(inst, d);
    }

    private boolean was_an_attack(Pair<String, my_date> p)
    {
        int count = 0;
        ArrayList<my_date> logs = instance_map.get(p.getKey());
        ArrayList<my_date> to_delete = new ArrayList<>();
        my_date curr_date = p.getValue();
        for (my_date log : logs){
            if (curr_date.same_minute(log))
            {
                count ++;
            }
            if (!(log.same_hour(curr_date)) || (log.same_hour(curr_date) && curr_date.minute - log.minute > NUM_OF_MINUTES_TO_CHECK))
            {
                to_delete.add(log);
            }

        }
        logs.removeAll(to_delete);
        return count >= LOGS_PER_MINUTE;
    }

   public void raise_flag(){}

    public void start()
    {
        String line = "";
//        String line = wait_for_input();
        Pair<String, my_date> res;
        if (check_if_suspect(line)){
            res = parse_line(line);
            if (!res.getKey().isEmpty())
            {
                if (was_an_attack(res))
                    raise_flag();
            }
        }



    }

    public static void main(String[] args)

    {
        UAAModule a = new UAAModule();
        a.parse_line("");
    }
}
