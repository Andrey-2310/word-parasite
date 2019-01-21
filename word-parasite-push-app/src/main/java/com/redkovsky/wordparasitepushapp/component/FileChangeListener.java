package com.redkovsky.wordparasitepushapp.component;

import com.redkovsky.wordparasitepushapp.service.FileManagerService;
import com.redkovsky.wordparasitepushapp.service.WebSocketTopicResolver;
import com.redkovsky.wordparasitepushapp.service.WordParasiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.util.logging.Level.SEVERE;

@Component
@RequiredArgsConstructor
public class FileChangeListener {

    private static final Logger LOGGER = Logger.getLogger(FileChangeListener.class.getName());

    @Value("${directory.path}")
    private String directoryAddress;

    @Value("${file.name}")
    private String filename;

    private final FileManagerService fileManagerService;
    private final WebSocketTopicResolver webSocketTopicResolver;
    private final WordParasiteService wordParasiteService;

    @EventListener(ApplicationReadyEvent.class)
    public void createFileListener() {
        final Path filePath = Paths.get(directoryAddress, filename);
        try(WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Paths.get(directoryAddress).register(watchService, ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                key.pollEvents().forEach(watchEvent -> {
                    final String newLine = fileManagerService.getNewFileLine(filePath);
                    webSocketTopicResolver.sendRecentlySaidWords(newLine);
                    webSocketTopicResolver.sendStatistics(wordParasiteService.receiveStatisticsWithNewLine(newLine));
                });
                key.reset();
            }
        } catch (IOException e) {
            LOGGER.log(SEVERE, e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.log(SEVERE, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
