package com.Avengers.app;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class UI_Module extends Interface_Module{

    public static  Map<String, Deque<String>> modules_alerts;

    public UI_Module(String module_name, Framework_Module fm, ArrayList<String> modules_names)
    {
        moduleName = module_name;
        sync_to = fm;
        modules_alerts = new HashMap<>();
        for (String module: modules_names)
        {
            modules_alerts.put(module, new LinkedList<>());
        }
    }


    @Override
    public void run() {
        while (true){
            synchronized (sync_to.modules_messages) {
                try {
                    sync_to.modules_messages.wait();
                    sync_to.getAlertData(modules_alerts);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }
            }
    }
}
