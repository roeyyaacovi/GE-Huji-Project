package com.Avengers.app;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Framework_Module  {
    private Map<String, Long> module_file_pos;
    private String input_path;
    private static final long INITIAL_VALUE = 0;
    private static final long END_OF_FILE = -1;

    Framework_Module(ArrayList<String> modules_names, String input_path)
    {
        for (String module_name: modules_names)
        {
            module_file_pos.put(module_name, INITIAL_VALUE);
        }
        this.input_path = input_path;
    }

    public void getData(String module_name, char[] buff, int buffer_size)
    {
        System.out.println("get data");
        BufferedReader reader = null;
        long pos = module_file_pos.get(module_name);
        long new_pos = pos;
        try {
            reader = new BufferedReader(new FileReader(input_path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (reader != null && pos != END_OF_FILE) {
                reader.skip(pos);
                new_pos = reader.read(buff, 0, buffer_size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        module_file_pos.put(module_name, new_pos);

    }

    public void alert(String module_name)
    {
        System.out.println(module_name + " alert");
    }

}
