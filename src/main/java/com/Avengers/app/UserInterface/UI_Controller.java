package com.Avengers.app.UserInterface;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UI_Controller {

    private static final String ATTRIBUTE_1 = "name";
    private static final String ATTRIBUTE_2 = "msg";

    @GetMapping("/status")
    private String status(Model model) {
        int i = 1;
        for (String module: UI_Module.modules_alerts.keySet()) {
            model.addAttribute(ATTRIBUTE_1 + Integer.toString(i), module);
            model.addAttribute(ATTRIBUTE_2 + Integer.toString(i), UI_Module.modules_alerts.get(module));
            i++;
        }
        return "status";
    }
}
