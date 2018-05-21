package com.Avengers.app.Framework;

import java.util.Map;

public class Module_Alert {
    private String module_name;
    private String alert_time;
    private String message;
    private Map<String, String> log;

    public Module_Alert(String module_name, String alert_time, String message, Map<String, String> log)
    {
        this.module_name = module_name;
        this.alert_time = alert_time;
        this.message = message;
        this.log = log;
    }

    public String getModule_name() {
        return module_name;
    }

    public String getAlert_time()
    {
        return alert_time;
    }

    public String getMessage()
    {
        return message;
    }

    public Map<String, String> getLog()
    {
        return log;
    }



}
