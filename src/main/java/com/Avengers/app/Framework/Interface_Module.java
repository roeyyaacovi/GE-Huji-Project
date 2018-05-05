package com.Avengers.app.Framework;

abstract public class Interface_Module extends Thread {

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
