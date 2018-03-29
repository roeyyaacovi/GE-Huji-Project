package com.Avengers.app;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



    public class UAAModule {
        private Map<String, Pair<String, String>> map;
        public UAAModule()
        {
            map = new HashMap<>();
        }

        public  void parseLine(String line)
        {
            String date, hour, inst;
            String pattern = "\"time\":\"(.*)T(.*)\\+.*inst\":\"(.*)\",\"tid.*";
            Pattern r = Pattern.compile(pattern);
            line = "{\"time\":\"2017-12-10T11:34:18.388+0000\",\"tnt\":\"495bf861-b1f8-4de7-a8d6-b8a96f392337\",\"corr\":\"b0ac39f8ecb10b0f\",\"appn\":\"cf3-staging-uaa\",\"dpmt\":\"88a65cea-5d1a-4c9d-86e0-c3842093c4af\",\"inst\":\"25d195dd-6120-4b89-579a-a7a9921c50cf\",\"tid\":\"http-nio-8080-exec-5\",\"mod\":\"JwtBearerAssertionAuthenticationFilter.java\",\"lvl\":\"DEBUG\",\"msg\":\"jwt-bearer authentication failed. Unknown client.\"}";
            Matcher m = r.matcher(line);
            if (m.find( )) {
                date = m.group(0);
                hour = m.group(1);
                inst = m.group(2);
                map.put(inst, new Pair<String, String>(date, hour));
            }
        }

        public void start()
        {

        }

        public static void main(String[] args)
        {
            parseLine("");
        }
    }

}
