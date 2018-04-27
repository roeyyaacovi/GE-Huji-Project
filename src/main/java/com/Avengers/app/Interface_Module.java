package com.Avengers.app;

import java.util.ArrayList;
import java.util.Map;

public abstract class Interface_Module extends Thread {

    protected Thread t;
    protected String moduleName;
    protected Framework_Module sync_to;

    abstract public void run();


    public void start() {
        if (t == null) {
            t = new Thread(this, moduleName);
            t.start();
        }
    }

}
