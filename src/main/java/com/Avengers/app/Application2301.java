package com.Avengers.app;

import com.Avengers.app.Security.Parser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application2301 {

    private static final Logger log = LoggerFactory.getLogger(Application2301.class);
    private static final String FILENAME = "students_dataset_uaa_attack.log";
    private static Connection conn = null;
//    private static String tableName = "features";
    private static String tableName = "features2";


    public static void createTable(ArrayList<ArrayList<ArrayList<String>>> lines)
    {
        String qs = "CREATE TABLE IF NOT EXISTS "+ tableName + "(" ;
        ArrayList<ArrayList<String>> line = lines.get(0);
        ArrayList<String> pair;
        for(int i=0; i<line.size(); i++)
        {
            pair = line.get(i);
            if(i == line.size() - 1)
            {
                qs += pair.get(0) + " varchar(20000)" ;
                break;

            }
            qs += pair.get(0) + " varchar(225)," ;

        }
        qs += ")";
        Statement st = null;
        int rs ;
        String q1,q2;


        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("connect");
        } catch (ClassNotFoundException cnfe){
            System.out.println("Could not find the JDBC driver!");
            System.exit(1);
        }
        try {
            //database location, database user, database password
            conn = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/testdb","postgres", "1234");
            st = conn.createStatement();
            rs = st.executeUpdate(qs);
        } catch (SQLException sqle) {
            System.out.println("Could not connect");
            System.exit(1);
        }

    }

    public static void insertIntoTable(ArrayList<ArrayList<ArrayList<String>>> attributes)
    {
        Statement st = null;
        int rs ;
        ArrayList<String> pair;
        String q1,q2;
        ArrayList<ArrayList<String>> line;
        for (int j=0; j<attributes.size(); j+=2)
        {
            line = attributes.get(j);
            q1 = "INSERT INTO " + tableName + "(";
            q2 = " VALUES (";
            boolean should_break = false;
            for(int i=0; i<line.size(); i++)
            {
                should_break = false;
                pair = line.get(i);
                if(i == line.size() - 1)
                {
                    q1 += pair.get(0);
//                    System.out.println(pair.get(0));
                    if(!pair.get(1).contains("88a65cea-5d1a-4c9d-86e0-c3842093c4af"))
                    {
                        should_break = true;
                        break;
                    }
                    if(pair.get(1).length() > 20000)
                    {
                        q2 += "'" + pair.get(1).substring(1, 20000).replaceAll("'","") + "'";
                        System.out.println(pair.get(1).length());
                    }
                    else {
                        q2 += "'" + pair.get(1).substring(1, pair.get(1).length() - 1).replaceAll("'", "") + "'";
                    }
                    break;

                }
                q1 += pair.get(0) + ",";
                q2 += "'" + pair.get(1).substring(1, pair.get(1).length() - 1).replaceAll("'","") + "'" + ",";
            }
            if(!should_break)
            {
                try {
                    st = conn.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                q1+=")";
                q2+=")";
                q1 += q2;
                try {
                    rs = st.executeUpdate(q1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ArrayList<ArrayList<Pair>> getData()
    {
        Statement st = null;
        ResultSet rs ;
        String q1,q2;
        ArrayList<Pair> line = new ArrayList<>();
        ArrayList<ArrayList<Pair>> lines = new ArrayList<>();
        String qs = "SELECT * FROM " + tableName;
        ArrayList<String> features_names = new ArrayList<>();

        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("connect");
        } catch (ClassNotFoundException cnfe){
            System.out.println("Could not find the JDBC driver!");
            System.exit(1);
        }
        try {
            //database location, database user, database password
            conn = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/testdb","postgres", "1234");
            st = conn.createStatement();
            rs = st.executeQuery(qs);
            ResultSetMetaData meta = rs.getMetaData();
            for (int i=1; i <= meta.getColumnCount(); i++)
            {
                features_names.add(meta.getColumnName(i));
            }
            Pair p;
            while (rs.next()) {
                line.clear();
                for (int i=1; i<= features_names.size(); i++)
                {
                    q1 = features_names.get(i-1);
                    q2 = rs.getString(i);
                    p = new Pair(q1, q2);
                    line.add(p);
                }
                lines.add(line);

            }
        } catch (SQLException sqle) {
            System.out.println("Could not connect");
            System.exit(1);
        }
        return lines;

    }


    public static void main( String[] args )
    {
        Parser parser = new Parser();



   //     SpringApplication.run(Application.class, args);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<ArrayList<String>>> attributes = ReadFile.getAttributesSet(br);
        createTable(attributes);
        while(!attributes.isEmpty())
        {
            insertIntoTable(attributes);
            attributes = ReadFile.getAttributesSet(br);
        }


    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new Customer("Magdolena", "Ippen"));

        };
    }
}


