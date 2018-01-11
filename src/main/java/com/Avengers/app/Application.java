package com.Avengers.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main( String[] args )
    {
   //     SpringApplication.run(Application.class, args);
        String tableName = "features";
        ArrayList<ArrayList<ArrayList<String>>> attributes = ReadFile.getAttributesSet();
        String qs = "CREATE TABLE IF NOT EXISTS "+ tableName + "(";
        ArrayList<ArrayList<String>> line = attributes.get(0);
        ArrayList<String> pair;
        for(int i=0; i<line.size(); i++)
        {
            pair = line.get(i);
            if(i == line.size() - 1)
            {
                qs += pair.get(0) + " varchar(2000)" ;
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
        Connection conn = null;
        try {
            //database location, database user, database password
            conn = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/testdb","postgres", "xsw21qaz");
            st = conn.createStatement();
            rs = st.executeUpdate(qs);
            for (int j=0; j<attributes.size(); j+=2)
            {
                line = attributes.get(j);
                q1 = "INSERT INTO " + tableName + "(";
                q2 = " VALUES (";
                for(int i=0; i<line.size(); i++)
                {
                    pair = line.get(i);
                    if(i == line.size() - 1)
                    {
                        q1 += pair.get(0);
                        q2 += "'" + pair.get(1).substring(1, pair.get(1).length() - 1).replaceAll("'","") + "'";
                        break;

                    }
                    q1 += pair.get(0) + ",";
                    q2 += "'" + pair.get(1).substring(1, pair.get(1).length() - 1).replaceAll("'","") + "'" + ",";
                }
                st = conn.createStatement();
                q1+=")";
                q2+=")";
                q1 += q2;
                rs = st.executeUpdate(q1);
            }
        } catch (SQLException sqle) {
            System.out.println("Could not connect");
            System.exit(1);
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


