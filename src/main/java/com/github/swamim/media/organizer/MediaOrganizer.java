package com.github.swamim.media.organizer;

import com.github.swamim.media.organizer.utils.NamedThreadFactory;
import com.github.swamim.media.exiftool.core.ExifParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.github.swamim.media.organizer.utils.FileNameUtils.getExtension;


public class MediaOrganizer {
    private final ExecutorService mediaFileProcessor;
    private final ExecutorService monitorExecutor;
    private final ExifParser parser;

    private OrganizerConfiguration configuration;
    private TaskMonitor taskMonitor;

    MediaOrganizer(OrganizerConfiguration configuration) {
        this.configuration = configuration;
        this.mediaFileProcessor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("Media Processor"));
        this.monitorExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("Task Monitor"));
        this.parser = new ExifParser(configuration.getExifToolPath());
        Runtime.getRuntime().addShutdownHook(new NamedThreadFactory("Shutdown Hook").newThread(this::shutdown));
    }

    public void run() {
        this.taskMonitor = new TaskMonitor(this);
        parser.init();
        configuration.getSourceDirectories().stream().map(Paths::get).forEach(this::processSourceDirectory);
    }

    private void processSourceDirectory(Path dir) {
        try (Stream<Path> paths = Files.find(dir, Integer.MAX_VALUE, (path, attributes) -> mediaFilesFilter(path.toFile()))) {
            paths.forEach(processFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            monitorExecutor.submit(taskMonitor);
        }
    }

    private Consumer<Path> processFile() {
        return path -> {
            String fileExtension = getExtension(path.toString()).toLowerCase();
            if (configuration.getVideoFileExtensions().contains(fileExtension)) {
                mediaFileProcessor.execute(new MediaTask(path, configuration, parser, configuration.getTargetVideoDirectories(), taskMonitor));
            } else {
                mediaFileProcessor.execute(new MediaTask(path, configuration, parser, configuration.getTargetImageDirectories(), taskMonitor));
            }
            taskMonitor.push();
        };
    }

    public void cancel() {
        shutdownExecutor(mediaFileProcessor);
        shutdownExecutor(monitorExecutor);
    }

    private void shutdownExecutor(ExecutorService mediaFileProcessor) {
        if (mediaFileProcessor != null) {
            mediaFileProcessor.shutdown();
            try {
                if (mediaFileProcessor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    mediaFileProcessor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean mediaFilesFilter(File file) {
        String fileExtension = getExtension(file.getName()).toLowerCase();
        return !file.isDirectory() &&
                (
                        configuration.getVideoFileExtensions().contains(fileExtension) ||
                        configuration.getImageFileExtensions().contains(fileExtension)
                );
    }

    private void shutdown() {
        if (parser != null) {
            parser.shutdown();
        }
    }
}
