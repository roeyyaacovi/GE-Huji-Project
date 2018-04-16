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
    private static final long SKIP_LINES = 2;

    Framework_Module(ArrayList<String> modules_names, String input_path)
    {
        for (String module_name: modules_names)
        {
            module_file_pos.put(module_name, INITIAL_VALUE);
        }
        this.input_path = input_path;
    }

    public int getData(String module_name, ArrayList<Map<String, String>> buff, int num_of_lines)
    {
        System.out.println("get data");
        BufferedReader reader = null;
        long pos = module_file_pos.get(module_name);
        long new_pos = pos;
        int lines_read = 0;
        try {
            reader = new BufferedReader(new FileReader(input_path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (reader != null && pos != END_OF_FILE) {
                if (pos == 0)
                {
                    for (int j=0; j< SKIP_LINES; j++)
                    {
                        reader.readLine();
                    }
                }
                else {
                    reader.skip(pos);
                }
                String line;
                Map<String, String> parsed_line;
                for (int i=0 ; i<num_of_lines; i++) {
                    if ((line = reader.readLine()) != null) {
                        lines_read ++;
                        new_pos += line.getBytes().length;
                        parsed_line = Read_File.getAttributesSet(line);
                        buff.add(parsed_line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        module_file_pos.put(module_name, new_pos);
        return lines_read;
    }

    public void alert(String module_name)
    {
        System.out.println(module_name + " alert");
    }

}
