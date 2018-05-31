package com.Avengers.app.UserInterface;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    private static String msg = "";

    @Autowired
    SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 1000)
    public void sendMessages() {
            if (msg != "") {
                template.convertAndSend("/topic/status", new Greeting(msg));
                msg = "";
        }
    }

    public static void setMsg(String msg1){

        msg = msg1;

    }


}
