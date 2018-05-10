package com.Avengers.app.UserInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UI_Alert {
    private String module_name;
    private String local_time;
    private String alert_time;
    private String alert_message;
    private String alert_log;

    public UI_Alert()
    {
        DateFormat dateFormat = new SimpleDateFormat();
        Calendar cal = Calendar.getInstance();
        local_time = dateFormat.format(cal);

    }

    public String getModule_name()
    {
        return module_name;
    }

    public String getLocal_time()
    {
        return local_time;
    }

    public String getAlert_time()
    {
        return alert_time;
    }

    public String getAlert_message() {
        return alert_message;
    }

    public String getAlert_log() {
        return alert_log;
    }
}
