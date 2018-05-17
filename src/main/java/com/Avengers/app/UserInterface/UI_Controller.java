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

    private static final String ATTRIBUTE_1 = "name";
    private static final String ATTRIBUTE_2 = "msg";

//    @GetMapping("/status")
    @MessageMapping("/status")
    @SendTo("/topic/status")
    //private Map<String, Deque<Module_Alert>> status(Model model) {
    private ArrayList<UI_Alert> status() {
//        int i = 1;
//        for (String module: UI_Module.modules_alerts.keySet()) {
//            model.addAttribute(ATTRIBUTE_1 + Integer.toString(i), module);
//            model.addAttribute(ATTRIBUTE_2 + Integer.toString(i), UI_Module.modules_alerts.get(module).getFirst());
//            i++;
//        }
//        return "status";
    //    return UI_Module.modules_alerts;
        ArrayList<UI_Alert> to_return = new ArrayList<>();
        String m2 = "module2";
        String m3 = "module3";
        String m4 = "module4";
        String m5 = "module5";
        for (String m : UI_Module.modules_alerts.keySet()) {
            to_return.add(new UI_Alert(m, UI_Module.modules_alerts.get(m)));
            to_return.add(new UI_Alert(m2, UI_Module.modules_alerts.get(m)));
            to_return.add(new UI_Alert(m3, UI_Module.modules_alerts.get(m)));
            to_return.add(new UI_Alert(m4, UI_Module.modules_alerts.get(m)));
            to_return.add(new UI_Alert(m5, UI_Module.modules_alerts.get(m)));
        }
        return to_return;
    }
}
