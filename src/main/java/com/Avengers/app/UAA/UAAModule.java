package com.Avengers.app.UAA;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;
import javafx.util.Pair;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UAAModule extends Interface_Module {
    private Map<String, ArrayList<my_date>> tenant_map;
    private static final int LOGS_PER_MINUTE = 5;
    private static final int NUM_OF_MINUTES_TO_CHECK = 0;
    private static final int NUM_OF_LINES_TO_GET = 10;
    private static final String MESSAGE_TEMPLATE = "UAA attack from ";



    public UAAModule(String module_name, Framework_Module fm)
    {
        moduleName = module_name;
        sync_to = fm;
        tenant_map = new HashMap<>();
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

        public String toString()
        {
            return Integer.toString(this.year) +" " + Integer.toString(this.month) + " " + Integer.toString(this.day) + "," +
                    Integer.toString(this.hour) + ":" + Integer.toString(this.minute) + ":" + Double.toString(this.second) + "\n";
        }
    }

    private boolean check_if_suspect(Map<String, String> line){
        String[] phrases = {"app_id","88a65cea-5d1a-4c9d-86e0-c3842093c4af","message",
                "Given client ID does not match authenticated client"};
        if (line.keySet().contains(phrases[0]) && line.keySet().contains(phrases[2])) {
            if (line.get(phrases[0]).toLowerCase().equals(phrases[1].toLowerCase())) {
                if (line.get(phrases[2]).toLowerCase().contains(phrases[3].toLowerCase()))
                    return true;
            }
        }
        return false;
    }

    private  Pair<String, my_date> parse_line(Map<String, String> line)
    {
        String msg = line.get("message");
        String inst = "";
        my_date d = new my_date();
        //String pattern = ".*\"time\":\"((\\d+)-(\\d+)-(\\d+))T((\\d+):(\\d+):(.*))\\+.*tnt\":\"(.*)\",\"corr.*";
        String pattern = ".*time[^\\d]+(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(.*)\\+.*tnt[^\\da-zA-Z]+([\\da-zA-Z-]+).*corr.*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        if (m.find( )) {
            inst = m.group(7);
            if (!tenant_map.containsKey(inst)){
                tenant_map.put(inst, new ArrayList<>());
            }
            d = new my_date(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Double.parseDouble(m.group(6)));
            tenant_map.get(inst).add(d);
        }
        return new Pair<>(inst, d);
    }

    private boolean was_an_attack(Pair<String, my_date> p)
    {
        int count = 0;
        ArrayList<my_date> logs = tenant_map.get(p.getKey());
        ArrayList<my_date> to_delete = new ArrayList<>();
        ArrayList<my_date> to_check = new ArrayList<>();
        my_date curr_date = p.getValue();
        for (my_date log : logs){
            if (curr_date.same_minute(log))
            {
                count ++;
                to_check.add(log);
            }
            if (!(log.same_hour(curr_date)) || (log.same_hour(curr_date) && curr_date.minute - log.minute > NUM_OF_MINUTES_TO_CHECK))
            {
                to_delete.add(log);
            }

        }
        if (count >= LOGS_PER_MINUTE)
        {
            logs.removeAll(to_check);
        }
        logs.removeAll(to_delete);
        return count >= LOGS_PER_MINUTE;
    }


    public void run()
    {
        ArrayList<Map<String, String>> lines = new ArrayList<>();
        Map<String, String> line;
        Pair<String, my_date> res;
        int count = 0;
        while ((count = sync_to.getData(moduleName, lines, NUM_OF_LINES_TO_GET)) != 0) {
            //sync_to.getData(moduleName, lines, NUM_OF_LINES_TO_GET);
            for (int i=0; i <count; i++) {
                line = lines.get(i);
                if (check_if_suspect(line)) {
                    res = parse_line(line);
                    if (!res.getKey().isEmpty()) {
                        if (was_an_attack(res)) {
                            // tenant_map.get(res.getKey()).clear();
                            Module_Alert ma = new Module_Alert(res.getValue().toString(), MESSAGE_TEMPLATE + res.getKey(), null);
                            sync_to.alert(res.getKey(), ma);
                        }
                    }
                }
            }
            lines.clear();
        }
        // System.out.println(tenant_map.keySet());
        // System.out.println(tenant_map.values());




    }

}
