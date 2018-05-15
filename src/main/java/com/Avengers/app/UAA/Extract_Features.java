package com.Avengers.app.UAA;

import com.Avengers.app.Framework.Parser;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract_Features {

    private Map<String, Integer> tenant_fail;
    private Map<String, Integer> tenant_success;
    private Map<String, ArrayList<Double>> tenant_time_differences;
    private Map<String, my_time> tenant_last_time_fail;
    private static final double TO_SECONDS = 60;




    public class my_time {
        private int hour;
        private int minute;
        private double second;

        public my_time(int h, int mi, double s) {
            hour = h;
            minute = mi;
            second = s;
        }

        public int getHour() {
            return hour;
        }
        public int getMinute(){
            return minute;
        }

        public double getSecond() {
            return second;
        }
    }

    public class Feature_Vector
    {
        private int max_num_fails;
        private int min_num_success;
        private double min_avg_differences;

        public Feature_Vector(int max_num_fails, int min_num_success, double min_avg_differences )
        {
            this.max_num_fails = max_num_fails;
            this.min_num_success = min_num_success;
            this.min_avg_differences = min_avg_differences;
        }

        public int getMax_num_fails()
        {
            return max_num_fails;
        }

        public int getMin_num_success()
        {
            return min_num_success;
        }

        public double getMin_avg_differences()
        {
            return min_avg_differences;
        }
    }

        private boolean check_if_fail(Map<String, String> line){
        String[] phrases = {"app_id","88a65cea-5d1a-4c9d-86e0-c3842093c4af","message",
                "Given client ID does not match authenticated client"};
        if (line.keySet().contains(phrases[0]) && line.keySet().contains(phrases[2])) {
            line.put(phrases[0], line.get(phrases[0]).replaceAll("\"", ""));
            if (line.get(phrases[0]).toLowerCase().equals(phrases[1].toLowerCase())) {
                if (line.get(phrases[2]).toLowerCase().contains(phrases[3].toLowerCase()))
                    return true;
            }
        }
        return false;
    }

    private boolean check_if_success(Map<String, String> line){
        String[] phrases = {"app_id","88a65cea-5d1a-4c9d-86e0-c3842093c4af","message",
                "ClientAuthenticationSuccess"};
        if (line.keySet().contains(phrases[0]) && line.keySet().contains(phrases[2])) {
            line.put(phrases[0], line.get(phrases[0]).replaceAll("\"", ""));
            if (line.get(phrases[0]).toLowerCase().equals(phrases[1].toLowerCase())) {
                if (line.get(phrases[2]).toLowerCase().contains(phrases[3].toLowerCase()))
                    return true;
            }
        }
        return false;
    }

    private void analyze_line(Map<String, String> line)
    {
        String msg = line.get("message");
        String inst = "";
        int h, mi;
        double s, diff;
        my_time last;
        String pattern = ".*time[^\\d]+(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(.*)\\+.*tnt[^\\da-zA-Z]+([\\da-zA-Z-]+).*corr.*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        if (m.find( )) {
            inst = m.group(7);
            h = Integer.parseInt(m.group(4));
            mi = Integer.parseInt(m.group(5));
            s = Double.parseDouble(m.group(6));
            if (check_if_fail(line))
            {
                if (!tenant_fail.containsKey(inst)) {
                    tenant_fail.put(inst, 0);
                }
                tenant_fail.put(inst, tenant_fail.get(inst) + 1);
                if (!tenant_last_time_fail.containsKey(inst)){
                    tenant_last_time_fail.put(inst, new my_time(h, mi, s));
                }else{
                    if (!tenant_time_differences.containsKey(inst))
                        tenant_time_differences.put(inst, new ArrayList<>());
                    last = tenant_last_time_fail.get(inst);
                    diff = (s - last.getSecond()) + ((mi - last.getMinute()) * TO_SECONDS) + ((h - last.getHour()) * TO_SECONDS *TO_SECONDS);
                    tenant_time_differences.get(inst).add(diff);
                    tenant_last_time_fail.put(inst, new my_time(h, mi, s));
                }
            }
            if (check_if_success(line))
            {
                if (!tenant_success.containsKey(inst)) {
                    tenant_success.put(inst, 0);
                }
                tenant_success.put(inst, tenant_success.get(inst) + 1);
            }
        }
    }

    public int find_max_fail()
    {
        int max_fails = -1;
        for (String tenant: tenant_fail.keySet())
        {
            if (tenant_fail.get(tenant) > max_fails)
                max_fails = tenant_fail.get(tenant);
        }
        return max_fails;
    }

    public int find_min_success(){
        boolean first = true;
        int min_success = 0;
        for (String tenant: tenant_success.keySet())
        {
            if (first) {
                min_success = tenant_success.get(tenant);
                first = false;
            }
            else {
                if (tenant_success.get(tenant) < min_success)
                    min_success = tenant_success.get(tenant);
            }
        }
        return min_success;

    }


    public double calculateAverage(ArrayList<Double> diff_list) {
        double sum = 0;
        if(!diff_list.isEmpty()) {
            for (Double d: diff_list) {
                sum += d;
            }
            return sum / diff_list.size();
        }
        return sum;
    }
    public double find_min_diffrences(){
        boolean first = true;
        double min_diff = 0;
        double avg_diff = 0;
        for (String tenant: tenant_time_differences.keySet())
        {
            avg_diff = calculateAverage(tenant_time_differences.get(tenant));
            if (first) {
                min_diff = avg_diff;
                first = false;
            }
            else {
                if (avg_diff < min_diff)
                    min_diff = avg_diff;
            }
        }
        return min_diff;

    }

    public Feature_Vector build_feature_vector()
    {
        return new Feature_Vector(find_max_fail(), find_min_success(), find_min_diffrences());
    }

    public void build_sample(String input_path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(input_path));
            if (reader != null ) {
                for (int k = 0; k < 2; k++) {
                    reader.readLine();
                }
                String line;
                Map<String, String> parsed_line = new HashMap<>();
                while ((line = reader.readLine()) != null) {
                    parsed_line = Parser.getAttributesSet(line);
                    analyze_line(parsed_line);
                    line = reader.readLine();
                    }
                }
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
