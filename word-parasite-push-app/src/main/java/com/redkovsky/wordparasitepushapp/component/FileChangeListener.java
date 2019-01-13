package com.redkovsky.wordparasitepushapp.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Component
@RequiredArgsConstructor
public class FileChangeListener {

    @Value("${directory.path}")
    private String directoryAddress;

    @Value("${file.name}")
    private String filename;

    private final FileManagerService fileManagerService;
    private final SimpMessagingTemplate template;

        public void createFileListener() {
        final Path filePath = Paths.get(directoryAddress + "/" + filename);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Paths.get(directoryAddress).register(watchService, ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                key.pollEvents().forEach(watchEvent ->  {
                    final List<String> content = fileManagerService.getFileContent(filePath);
                    sendAsyncMessage(content.get(content.size() - 1));
                });
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }

    private void sendAsyncMessage(final Object content) {
        template.convertAndSend("/topic/voice", content);
    }
}
