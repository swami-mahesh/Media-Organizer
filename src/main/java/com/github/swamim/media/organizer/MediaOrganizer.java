package com.github.swamim.media.organizer;

import com.github.swamim.media.exiftool.core.ExifParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.github.swamim.media.organizer.utils.FileNameUtils.getExtension;


public class MediaOrganizer {
    private final ExecutorService mediaFileProcessor;
    private final ExifParser parser;

    private OrganizerConfiguration configuration;
    private TaskMonitor taskMonitor;

    MediaOrganizer(OrganizerConfiguration configuration) {
        this.configuration = configuration;
        this.mediaFileProcessor = configuration.getThreadPool();
        int numExifToolInstances = configuration.isSameThreadExecutor() ? 1 : Runtime.getRuntime().availableProcessors()+1;
        this.parser = new ExifParser(configuration.getExifToolPath(), numExifToolInstances);
    }

    public void run() {
        this.taskMonitor = new TaskMonitor(this, configuration.isSameThreadExecutor());
        parser.init();
        configuration.getSourceDirectories().stream().map(Paths::get).forEach(this::processSourceDirectory);
    }

    private void processSourceDirectory(Path dir) {
        try (Stream<Path> paths = Files.find(dir, Integer.MAX_VALUE, (path, attributes) -> mediaFilesFilter(path.toFile()))) {
            paths.forEach(processFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ForkJoinPool.commonPool().submit(taskMonitor);
        }
    }

    private Consumer<Path> processFile() {
        return path -> {
            String fileExtension = getExtension(path.toString()).toLowerCase();
            if (configuration.getVideoFileExtensions().contains(fileExtension)) {
                new MediaTask(path, configuration, parser, configuration.getTargetVideoDirectories(), taskMonitor, mediaFileProcessor).run();
            } else {
                new MediaTask(path, configuration, parser, configuration.getTargetImageDirectories(), taskMonitor, mediaFileProcessor).run();
            }
            taskMonitor.push();
        };
    }

    void cancel() {
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

    public void shutdown() {
        if (parser != null) {
            parser.shutdown();
        }
    }
}
