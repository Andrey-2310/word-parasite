package com.redkovsky.wordparasitepushapp.controller;

import com.redkovsky.wordparasitepushapp.component.FileChangeListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ListeningController {

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
