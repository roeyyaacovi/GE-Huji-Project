package com.Avengers.app;

import java.util.ArrayList;
import java.util.Map;

class Interface_Module extends Thread {

    private Thread t;
    private String moduleName;
    private Framework_Module sync_to;

    Interface_Module() {}
    public Interface_Module(String m, Framework_Module s)
    {
        moduleName = m;
        sync_to = s;
    }

    public void run()
    {
        ArrayList<Map<String, String>> p = new ArrayList<>();
        sync_to.getData(moduleName, p, 1);
        System.out.println(moduleName);
        System.out.println(p.get(0));
        sync_to.getData(moduleName, p, 1);
        System.out.println(moduleName);
        System.out.println(p.get(1));
        sync_to.alert(moduleName);

    }

    public void start() {
        if (t == null) {
            t = new Thread(this, moduleName);
            t.start();
        }
    }

}
