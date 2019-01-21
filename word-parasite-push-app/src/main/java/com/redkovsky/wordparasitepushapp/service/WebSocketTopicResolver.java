package com.redkovsky.wordparasitepushapp.service;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WebSocketTopicResolver {

    private final SimpMessagingTemplate template;

    public void sendRecentlySaidWords(final String content) {
        template.convertAndSend("/topic/voice", content);
    }

    public void sendStatistics(final Map<String, Integer> statistics) {
        template.convertAndSend("/topic/statistics", statistics.entrySet().stream()
                .filter(stringIntegerEntry -> stringIntegerEntry.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}
