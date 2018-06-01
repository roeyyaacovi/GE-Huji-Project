package com.Avengers.app;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

//import com.Avengers.app.SQLI.SQLDetector;
import com.Avengers.app.UserInterface.UI_Module;
import com.Avengers.app.Framework.Framework_Module;
import com.Avengers.app.Framework.Interface_Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Start_Tool {

    private static final String PACKAGE_NAME = "com.Avengers.app.";
    private static final String HTML_TEMPLATE = "src\\main\\resources\\templates\\status_template.html";
    private static final String HTML_FINAL = "src\\main\\resources\\templates\\status.html";

    private static void make_html_file(int modules_num) {
        BufferedReader br = null;
        BufferedWriter pw = null;
        boolean write_modules_num = false;

        try {
            br = new BufferedReader(new FileReader(HTML_TEMPLATE));
            pw = new BufferedWriter(new FileWriter(HTML_FINAL));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("<body>")) {
                    write_modules_num = true;
                    pw.write(line);
                    pw.newLine();
                    continue;
                }
                if (line.toLowerCase().contains("</body>")) {
                    write_modules_num = false;
                    pw.write(line);
                    pw.newLine();
                    continue;
                }
                if (write_modules_num){
                    for (int i=1 ; i<= modules_num; i++) {
                        pw.write(line.replaceAll("1", Integer.toString(i)));
                        pw.newLine();
                    }
                }else{
                    pw.write(line);
                    pw.newLine();
                }
            }
            br.close();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length >= 2) {
            String modules_demands_file = args[0];
            String log_file = args[1];
            try {
                BufferedReader br = new BufferedReader(new FileReader(modules_demands_file));
                ArrayList<Interface_Module> modules_threads = new ArrayList<>();
                ArrayList<String> modules_names = new ArrayList<>();
                String module_name;
                Class c;
                Constructor m;
                Object o;
                while ((module_name= br.readLine()) != null) {
                    modules_names.add(module_name);
                }
                Framework_Module fm = new Framework_Module(modules_names, log_file);
//                make_html_file(modules_names.size());
//                Interface_Module UIm = new UI_Module("UI_Module", fm, modules_names);
//                UIm.start();
//                modules_threads.add(UIm);
//                SpringApplication.run(Start_Tool.class, args);
                for (String mname: modules_names) {
                    c = Class.forName(PACKAGE_NAME + mname);
                    m = c.getConstructor(String.class, Framework_Module.class);
                    o = m.newInstance(mname, fm);
                    Interface_Module new_module = Interface_Module.class.cast(o);
                    new_module.start();
                    modules_threads.add(new_module);
                }
                for (Interface_Module module : modules_threads) {
                    module.join();
                }
//                UIm.join();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
