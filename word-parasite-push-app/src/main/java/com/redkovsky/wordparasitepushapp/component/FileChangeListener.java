package com.redkovsky.wordparasitepushapp.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
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
    private final SimpMessagingTemplate template;

    @EventListener(ApplicationReadyEvent.class)
    public void createFileListener() {
        final Path filePath = Paths.get(directoryAddress, filename);
        try(WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Paths.get(directoryAddress).register(watchService, ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                key.pollEvents().forEach(watchEvent -> {
                    final List<String> content = fileManagerService.getFileContent(filePath);
                    sendAsyncMessage(content.get(content.size() - 1));
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

    private void sendAsyncMessage(final Object content) {
        template.convertAndSend("/topic/voice", content);
    }
}
