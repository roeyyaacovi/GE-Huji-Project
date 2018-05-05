package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;

import java.util.*;


public class UI_Module extends Interface_Module {

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
