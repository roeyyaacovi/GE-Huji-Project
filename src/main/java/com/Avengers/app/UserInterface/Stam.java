package com.Avengers.app.UserInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Stam {

    private String module_name;
    private String local_time;

    public Stam() {
    }

    public Stam(String content) {
        this.module_name = content;
        DateFormat dateFormat = new SimpleDateFormat();
        Calendar cal = Calendar.getInstance();
        this.local_time = dateFormat.format(cal.getTime());
    }

    public String getContent() {
        return module_name;
    }

}

