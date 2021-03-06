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

    private static ArrayList<String> messages = new ArrayList<>();
    private static boolean connected = false;

    @Autowired
    SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 1000)
    public void sendMessages() {
            if (messages.size() >0 && connected) {
                for (String msg: messages)
                    template.convertAndSend("/topic/status", new Greeting(msg));
                messages.clear();
        }
    }

    public static void setMsg(String msg1){

        messages.add(msg1);

    }

    public static void setConnected(){
        connected = true;
    }


}
