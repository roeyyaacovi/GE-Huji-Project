package com.Avengers.app;

import java.util.ArrayList;
import java.util.Map;

public class Start_Tool {
    public static void main(String[] args)
    {
        ArrayList<String> a = new ArrayList<>();
        a.add("stam");
        Framework_Module f = new Framework_Module(a, "C:\\Users\\uzer1\\Desktop\\2018\\students_dataset_uaa_attack.log" );
        ArrayList<Map<String, String>> lines = new ArrayList<>();
        f.getData("stam" ,lines, 1);
//        Interface_Module t1 = new Interface_Module("a",f);
//        t1.start();
    }
}
