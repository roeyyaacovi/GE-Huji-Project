package com.Avengers.app;

public class Start_Tool {
    public static void main(String[] args)
    {
        Framework_Module f = new Framework_Module();
        Interface_Module t1 = new Interface_Module("a",f);
        t1.start();
    }
}
