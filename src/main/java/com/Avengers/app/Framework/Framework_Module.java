package com.Avengers.app.Framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Framework_Module  {
    private Map<String, Long> module_file_pos;
    public ArrayList<Module_Alert> modules_messages;
    private boolean new_alert = true;
    private String input_path;
    private static final long INITIAL_VALUE = 0;
    private static final long END_OF_FILE = -1;
    private static final long SKIP_LINES = 2;
    private static final int ERROR_SIGN = -1;

    public Framework_Module(ArrayList<String> modules_names, String input_path)
    {
        module_file_pos = new HashMap<>();
        modules_messages = new ArrayList<>();
        for (String module_name: modules_names)
        {
            module_file_pos.put(module_name, INITIAL_VALUE);
        }
        this.input_path = input_path;
    }

    public synchronized int getData(String module_name, ArrayList<Map<String, String>> buff, int num_of_lines)
    {
        //System.out.println("get data " + module_name);
        BufferedReader reader = null;
        long pos = module_file_pos.get(module_name);
        long new_pos = pos;
        int lines_read = 0;
        int lines_to_return = 0;
        try {
            reader = new BufferedReader(new FileReader(input_path));
            if (reader != null && pos != END_OF_FILE) {
                for (int k=0 ; k< pos + SKIP_LINES; k++)
                {
                    reader.readLine();
                }
                String line;
                Map<String, String> parsed_line = new HashMap<>();
                for (int i=0 ; i<num_of_lines; i++) {
                    if ((line = reader.readLine()) != null) {
                        lines_read ++;
                        lines_to_return++;
                        parsed_line = Parser.getAttributesSet(line);
                        buff.add(parsed_line);
                        line = reader.readLine();
                        lines_read++;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR_SIGN;
        }
        new_pos += lines_read;
        module_file_pos.put(module_name, new_pos);
        return lines_to_return;
    }

    public synchronized void getAlertData(Deque<Module_Alert> UI_msg)
    {
        synchronized (modules_messages) {
            for (Module_Alert module_alert : modules_messages) {
                UI_msg.addFirst(module_alert);
            }
            modules_messages.clear();
        }

    }
    public synchronized void alert(Module_Alert module_alert)
    {

        synchronized (modules_messages) {
            modules_messages.add(module_alert);
            modules_messages.notifyAll();
        }

    }

}
