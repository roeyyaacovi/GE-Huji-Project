//package com.Avengers.app.UserInterface;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.ArrayList;
//
//@EnableScheduling
//@Configuration
//public class SchedulerConfig {
//
//    @Autowired
//    SimpMessagingTemplate template;
//
//    @Scheduled(fixedDelay = 3000)
//    public void sendMessages() {
//        ArrayList<UI_Alert> to_return = new ArrayList<>();
//        for (String m : UI_Module.modules_alerts.keySet()) {
//            to_return.add(new UI_Alert(m, UI_Module.modules_alerts.get(m)));
//        }
//        template.convertAndSend("/topic/status", to_return.get(0));
//    }
//}
