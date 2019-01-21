package com.redkovsky.wordparasitepushapp.service;

import com.google.common.collect.Iterables;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
public class FileManagerService {

    private static final Logger LOGGER = Logger.getLogger(FileManagerService.class.getName());

    public String getNewFileLine(final Path path) {
        try {
            return Iterables.getLast(Files.readAllLines(path));
        } catch (IOException e) {
            LOGGER.log(SEVERE, e.getMessage());
        }
        return EMPTY;
    }
}
