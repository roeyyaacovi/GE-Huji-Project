package com.Avengers.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Start_Tool {

    private static final String package_name = "com.Avengers.app.";

    public static void main(String[] args) {
        if (args.length >= 2) {
            SpringApplication.run(Start_Tool.class, args);
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
                for (String mname: modules_names) {
                    c = Class.forName(package_name + mname);
                    m = c.getConstructor(String.class, Framework_Module.class);
                    o = m.newInstance(mname, fm);
                    Interface_Module new_module = Interface_Module.class.cast(o);
                    new_module.start();
                    modules_threads.add(new_module);
                }
                for (Interface_Module module : modules_threads) {
                    module.join();
                }
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
