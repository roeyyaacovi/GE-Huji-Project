package com.Avengers.app.Framework;

import java.util.Map;

public class Module_Alert {
    private String alert_time;
    private String message;
    private Map<String, String> log;

    public Module_Alert(String alert_time, String message, Map<String, String> log)
    {
        this.alert_time = alert_time;
        this.message = message;
        this.log = log;
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


    void setAlert_time(String alert_time)
    {
        this.alert_time = alert_time;
    }

    void setMessage(String message)
    {
        this.message = message;
    }

//    void setLog(Map<String, String> log)
//    {
//        this.log = log;
//    }
}
