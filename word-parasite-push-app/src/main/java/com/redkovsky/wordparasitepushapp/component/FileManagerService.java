package com.redkovsky.wordparasitepushapp.component;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

@Service
public class FileManagerService {

    private static final Logger LOGGER = Logger.getLogger(FileManagerService.class.getName());

    public List<String> getFileContent(final Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            LOGGER.log(SEVERE, e.getMessage());
        }
        return Collections.emptyList();
    }
}
