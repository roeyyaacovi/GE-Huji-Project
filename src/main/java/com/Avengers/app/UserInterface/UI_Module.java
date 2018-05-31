package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class UI_Module extends Interface_Module {

    private Deque<Module_Alert> modules_alerts;

    public UI_Module(String module_name, Framework_Module fm, ArrayList<String> modules_names)
    {
        moduleName = module_name;
        sync_to = fm;
        modules_alerts = new LinkedList<>();
    }


    @Override
    public void run() {
        while (true){
            synchronized (sync_to.modules_messages) {
                try {
                    sync_to.modules_messages.wait();
                    sync_to.getAlertData(modules_alerts);
                    for(Module_Alert module_alert: modules_alerts)
                    {
                        UI_Alert ui_alert = new UI_Alert(module_alert);
                        SchedulerConfig.setMsg(ui_alert.getContent());
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                    modules_alerts.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }
            }
    }
}
