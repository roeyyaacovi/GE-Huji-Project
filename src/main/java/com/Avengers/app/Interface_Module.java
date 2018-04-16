package com.Avengers.app;

abstract class Interface_Module extends Thread {

    private Thread t;
    private String moduleName;
    private Framework_Module sync_to;


    abstract public void run();

    public void start() {
        if (t == null) {
            t = new Thread(this, moduleName);
            t.start();
        }
    }

}
