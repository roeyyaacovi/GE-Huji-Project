package com.Avengers.app;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;


@Controller
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
