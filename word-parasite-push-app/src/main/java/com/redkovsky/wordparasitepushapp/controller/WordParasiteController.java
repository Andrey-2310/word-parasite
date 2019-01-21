package com.redkovsky.wordparasitepushapp.controller;

import com.redkovsky.wordparasitepushapp.service.WebSocketTopicResolver;
import com.redkovsky.wordparasitepushapp.service.WordParasiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class WordParasiteController {

    private final WordParasiteService wordParasiteService;
    private final WebSocketTopicResolver webSocketTopicResolver;

    @MessageMapping("/add")
    public void add(final String newWord) {
        wordParasiteService.addWordParasite(newWord);
    }

    @MessageMapping("/clean")
    public void clean() {
       webSocketTopicResolver.sendStatistics(wordParasiteService.cleanStatistics());
    }

}
