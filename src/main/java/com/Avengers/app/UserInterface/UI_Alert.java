package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Module_Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.Map;


public class UI_Alert {
    private String content;



    public UI_Alert(Module_Alert module_alert)
    {
        DateFormat dateFormat = new SimpleDateFormat();
        Calendar cal = Calendar.getInstance();
        String local_time = dateFormat.format(cal.getTime());
        this.content = "module name: " + module_alert.getModule_name() + " " + "local time: " + local_time + " " + "event time: " + module_alert.getAlert_time() + " " +
                "message: " + module_alert.getMessage(); // + " " + "log: " + module_alert.getLog();



    }

    public String getContent() {
        return content;
    }


}
