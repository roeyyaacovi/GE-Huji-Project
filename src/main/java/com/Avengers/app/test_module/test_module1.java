package com.Avengers.app.test_module;

import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import com.Avengers.app.Framework.Module_Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class test_module1 extends Interface_Module {

    public test_module1(String s, Framework_Module fm)
    {
        moduleName = s;
        sync_to = fm;
    }
    @Override
    public void run() {
        ArrayList<Map<String, String>> line = new ArrayList<>();
        sync_to.getData(moduleName, line , 1 );
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sync_to.alert(new Module_Alert(moduleName,"14:30", "hello", line.get(0)));

    }
}
