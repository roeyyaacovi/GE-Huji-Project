package com.Avengers.app.UAA;

import com.Avengers.app.Framework.Parser;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract_Features {

    private static Map<String, Integer> tenant_fail;
    private static Map<String, Integer> tenant_success;
    private static Map<String, ArrayList<Double>> tenant_time_differences;
    private static Map<String, My_Time> tenant_last_time_fail;
    private static final double TO_SECONDS = 60;


        private static boolean check_if_fail(Map<String, String> line){
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

    private static boolean check_if_success(Map<String, String> line){
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

    private static void analyze_line(Map<String, String> line)
    {
        String msg = line.get("message");
        String inst = "";
        int h, mi;
        double s, diff;
        My_Time last;
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
                    tenant_last_time_fail.put(inst, new My_Time(h, mi, s));
                }else{
                    if (!tenant_time_differences.containsKey(inst))
                        tenant_time_differences.put(inst, new ArrayList<>());
                    last = tenant_last_time_fail.get(inst);
                    diff = (s - last.getSecond()) + ((mi - last.getMinute()) * TO_SECONDS) + ((h - last.getHour()) * TO_SECONDS *TO_SECONDS);
                    tenant_time_differences.get(inst).add(diff);
                    tenant_last_time_fail.put(inst, new My_Time(h, mi, s));
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

    public static Pair<String, Integer> find_max_fail()
    {
        int max_fails = 0;
        String max_tenant = "";
        boolean first = true;
        for (String tenant: tenant_fail.keySet())
        {
            if (first)
            {
                max_fails = tenant_fail.get(tenant);
                max_tenant = tenant;
                first = false;
            }
            else {
                if (tenant_fail.get(tenant) > max_fails)
                    max_fails = tenant_fail.get(tenant);
            }
        }

        return new Pair<>(max_tenant, max_fails);
    }

    public static int find_max_success()
    {
        int max_fails = 0;
        String max_tenant = "";
        boolean first = true;
        for (String tenant: tenant_success.keySet())
        {
            if (first)
            {
                max_fails = tenant_success.get(tenant);
                max_tenant = tenant;
                first = false;
            }
            else {
                if (tenant_success.get(tenant) > max_fails)
                    max_fails = tenant_success.get(tenant);
            }
        }

        return max_fails;
    }



    public static double calculateAverage(ArrayList<Double> diff_list) {
        double sum = 0;
        if(!diff_list.isEmpty()) {
            for (Double d: diff_list) {
                sum += d;
            }
            return sum / diff_list.size();
        }
        return sum;
    }
    public static double find_min_diffrences(){
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



    public static Features_Vector build_sample(String input_path, String label) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(input_path));
            if (reader != null ) {
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
        return get_features_vector(label);
    }

    public static void initialize_globals()
    {
        tenant_time_differences = new HashMap<>();
        tenant_success = new HashMap<>();
        tenant_fail = new HashMap<>();
        tenant_last_time_fail = new HashMap<>();
    }

    public static void clear_globals()
    {
        tenant_last_time_fail.clear();
        tenant_fail.clear();
        tenant_success.clear();
        tenant_time_differences.clear();
    }

    public static void build_samples(String output_file, String malicious_directory, String vanilla_directory)
    {
        String label;
        File[] malicious_files = new File(malicious_directory).listFiles();
        File[] vanilla_files = new File(vanilla_directory).listFiles();
        Features_Vector feature_vector;
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file), "utf-8"));
            label = "+";
            for(File file: malicious_files) {
                feature_vector = build_sample("malicious/"+file.getName(), label);
                writer.write(feature_vector.toString() + "\n");
                clear_globals();
            }
            label = "-";
            for(File file: vanilla_files) {
                feature_vector = build_sample("vanilla/"+file.getName(), label);
                writer.write(feature_vector.toString() + "\n");
                clear_globals();
            }
        } catch (IOException ex) {

        } finally {
            try {
                writer.close();
            } catch (Exception ex) {}
        }
    }



    public static void build_all(String output_file, String malicious_directory, String vanilla_directory) {
        initialize_globals();
        build_samples(output_file, malicious_directory, vanilla_directory);

    }

    public static void build_line_by_line (Map<String, String> line)
    {
        analyze_line(line);
    }

    public static Features_Vector get_features_vector(String label)
    {
        Pair<String, Integer> max_fail = find_max_fail();
        int success;
        if (tenant_success.keySet().contains(max_fail.getKey()))
        {
            success = tenant_success.get(max_fail.getKey());
        }
        else
            success = 0;
        return new Features_Vector(max_fail.getValue(), success,find_min_diffrences(), label);
    }


}
