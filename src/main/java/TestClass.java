package com.Avengers.app;

import java.util.ArrayList;
import java.util.Map;

public class TestClass extends Interface_Module {

    public TestClass(String a, Framework_Module fm)
    {
        moduleName = a;
        sync_to = fm;
    }


    public void run() {
        System.out.println("hola from Test");
//        sync_to.alert(moduleName);
//        sync_to.getData()
    }
}
