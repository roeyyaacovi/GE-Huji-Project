package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Module_Alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.Map;

public class UI_Alert {
    private String module_name;
    private String local_time;
    private Deque<Module_Alert> module_alerts;
//    private String alert_time;
//    private String alert_message;
//    private Map<String, String> alert_log;

    public UI_Alert(String module_name, Deque<Module_Alert> module_alerts)
    {
        DateFormat dateFormat = new SimpleDateFormat();
        Calendar cal = Calendar.getInstance();
        this.local_time = dateFormat.format(cal.getTime());
        this.module_name = module_name;
        this.module_alerts = module_alerts;
//        this.alert_time = module_alert.getAlert_time();
//        this.alert_message = module_alert.getMessage();
//        this.alert_log = module_alert.getLog();

    }

    public String getModule_name()
    {
        return module_name;
    }

    public String getLocal_time()
    {
        return local_time;
    }

    public Deque<Module_Alert> getModule_alerts(){
        return module_alerts;
    }

//    public String getAlert_time()
//    {
//        return alert_time;
//    }
//
//    public String getAlert_message() {
//        return alert_message;
//    }
//
//    public Map<String, String> getAlert_log() {
//        return alert_log;
//    }
}
