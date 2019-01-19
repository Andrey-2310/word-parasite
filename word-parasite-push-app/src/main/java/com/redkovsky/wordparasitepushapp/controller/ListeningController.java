package com.redkovsky.wordparasitepushapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListeningController {

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
