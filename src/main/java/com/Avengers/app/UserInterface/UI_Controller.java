package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Module_Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;


@RestController
public class UI_Controller {




    @MessageMapping("/status")
    @SendTo("/topic/status")
    public Greeting greeting(HelloMessage message) throws Exception {
        //Thread.sleep(1000); // simulated delay
        if (message.getName().equals("connected"))
        {
            SchedulerConfig.setConnected();
        }
        return new Greeting(HtmlUtils.htmlEscape(message.getName()) );
        //return new Greeting(message.getName());
    }
}
