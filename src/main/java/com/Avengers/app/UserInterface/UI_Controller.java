package com.Avengers.app.UserInterface;

import com.Avengers.app.Framework.Module_Alert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;


@RestController
public class UI_Controller {

    private static final String ATTRIBUTE_1 = "name";
    private static final String ATTRIBUTE_2 = "msg";

    @GetMapping("/status")
    //private Map<String, Deque<Module_Alert>> status(Model model) {
    private String status(Model model) {
        int i = 1;
        for (String module: UI_Module.modules_alerts.keySet()) {
            model.addAttribute(ATTRIBUTE_1 + Integer.toString(i), module);
            model.addAttribute(ATTRIBUTE_2 + Integer.toString(i), UI_Module.modules_alerts.get(module).getFirst());
            i++;
        }
        return "status";
    //    return UI_Module.modules_alerts;
    }
}
