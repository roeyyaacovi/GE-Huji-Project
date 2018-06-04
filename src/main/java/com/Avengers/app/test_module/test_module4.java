//package com.Avengers.app.test_module;
//
//import com.Avengers.app.Framework.Framework_Module;
//import com.Avengers.app.Framework.Interface_Module;
//import com.Avengers.app.Framework.Module_Alert;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class test_module4 extends Interface_Module {
//
//    public test_module4(String s, Framework_Module fm)
//    {
//        moduleName = s;
//        sync_to = fm;
//    }
//    @Override
//    public void run() {
//        ArrayList<Map<String, String>> line = new ArrayList<>();
//        sync_to.getData(moduleName, line , 1 );
//        sync_to.alert(moduleName, new Module_Alert("16:30", "orange", line.get(0)));
//
//    }
//}