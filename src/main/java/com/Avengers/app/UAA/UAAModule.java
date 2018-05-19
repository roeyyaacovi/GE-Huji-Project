package com.Avengers.app.UAA;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UAAModule extends Interface_Module {

    private static Map<String, Integer> tenant_fail;
    private static Map<String, Integer> tenant_success;
    private static Map<String, ArrayList<Double>> tenant_time_differences;
    private static Map<String, My_Time> tenant_last_time_fail;
    private static final double TO_SECONDS = 60;
    private static final String MESSAGE_TEMPLATE = "UAA attack from ";
    private boolean flag = true;

    public UAAModule(String moduleName, Framework_Module fm)
    {
        this.moduleName = moduleName;
        this.sync_to = fm;
    }

    public Pair<String, My_Time> extract_time(Map<String, String> line)
    {
        String msg = line.get("message");
        int h, mi;
        Double s;
        String inst;
        String pattern = ".*time[^\\d]+(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(.*)\\+.*tnt[^\\da-zA-Z]+([\\da-zA-Z-]+).*corr.*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        if (m.find( )) {
            inst = m.group(7);
            h = Integer.parseInt(m.group(4));
            mi = Integer.parseInt(m.group(5));
            s = Double.parseDouble(m.group(6));
            return new Pair<>(inst,  new My_Time(h, mi, s));
        }
        return null;
    }

    public Pair<My_Time, Map<String, String>> find_first_time()
    {
        ArrayList<Map<String, String>> lines = new ArrayList<>();
        My_Time t;
        while(sync_to.getData(moduleName, lines, 1) >= 0)
        {
            if ((t=extract_time(lines.get(0)).getValue()) != null)
            {
                return new Pair<>(t, lines.get(0));
            }
        }
        flag = false;
        return null;
    }


    @Override
    public void run() {
        Extract_Features.build_samples("samples.txt", "malicious", "vanilla");
        ID3.main(null);
        ArrayList<Map<String, String>> lines = new ArrayList<>();
        Map<String, String> line;
        int numLines = 0;
        My_Time first_log_time, log_time;
        Pair<String, My_Time> tntNtime;
        Pair<My_Time, Map<String, String>> p;
        Features_Vector features_vector;
        Extract_Features.initialize_globals();
        int prediction;
        p = find_first_time();
        while(flag)
        {
            first_log_time = p.getKey();
            line = p.getValue();
            Extract_Features.build_line_by_line(line);
            while(sync_to.getData(moduleName, lines, 1) > 0)
            {
                line = lines.get(0);
                if ((tntNtime = extract_time(line)) != null) {
                    log_time = tntNtime.getValue();
                    if (first_log_time.time_difference(log_time) <= TO_SECONDS) {
                        Extract_Features.build_line_by_line(line);
                    } else {
                        features_vector = Extract_Features.get_features_vector("+");
                        Extract_Features.clear_globals();
                        prediction = ID3.root.classify(new Instance(features_vector.toString(), 0));
                        if (prediction == 1) {
                            Module_Alert ma = new Module_Alert(log_time.toString(), MESSAGE_TEMPLATE + tntNtime.getKey(), line);
                            sync_to.alert(moduleName, ma);
                        }
                        p = new Pair<>(log_time, line);
                        break;

                    }
                }else {
                    p = find_first_time();
                }
            }


        }

    }
}
