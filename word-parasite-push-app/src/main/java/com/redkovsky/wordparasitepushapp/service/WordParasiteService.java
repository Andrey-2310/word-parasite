package com.redkovsky.wordparasitepushapp.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WordParasiteService {

    private Map<String, Integer> statistics = new HashMap<>();

    private List<String> wordParasites = new ArrayList<>(Arrays.asList(
            "короче", "однако", "это", "типа", "как бы", "это самое", "как сказать", "в общем-то", "знаешь", "ну",
            "то есть", "так сказать", "понимаешь", "собственно", "в принципе", "допустим", "например", "слушай",
            "собственно говоря", "кстати", "вообще", "вероятно", "значит", "на самом деле", "просто", "сложно сказать",
            "вот", "ладно", "блин", "так", "походу"
    ));


    @PostConstruct
    public void init() {
        wordParasites.forEach(wordParasite -> statistics.put(wordParasite, 0));
    }

    public Map<String, Integer> receiveStatisticsWithNewLine(final String newLine) {
        final String editedLine = processLine(newLine);
        wordParasites.forEach(wordParasite -> {
            if (editedLine.contains(wordParasite)) {
                statistics.merge(wordParasite, 1, Integer::sum);
            }
        });
        return statistics;
    }

    public void addWordParasite(final String word) {
        final String editedWord = processLine(word);
        if (!wordParasites.contains(editedWord)) {
            wordParasites.add(editedWord);
            statistics.put(editedWord, 0);
        }
    }

    public Map<String, Integer> cleanStatistics() {
        statistics.forEach((key, value) -> statistics.put(key, 0));
        return statistics;
    }

    private String processLine(final String newLine) {
        return newLine.toLowerCase().trim();
    }

}

