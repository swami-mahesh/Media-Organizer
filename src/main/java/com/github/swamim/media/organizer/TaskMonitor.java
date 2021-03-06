package com.github.swamim.media.organizer;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

public class TaskMonitor implements Runnable {
    private static Logger logger = Logger.getLogger(TaskMonitor.class);

    private final AtomicLong queuedTasks = new AtomicLong(0);
    private final MediaOrganizer mediaOrganizer;
    private final boolean skipMonitoring;

    public TaskMonitor(MediaOrganizer mediaOrganizer, boolean skipMonitoring) {
        this.mediaOrganizer = mediaOrganizer;
        this.skipMonitoring = skipMonitoring;
    }

    void push() {
        queuedTasks.getAndIncrement();
    }

    void pop() {
        queuedTasks.getAndDecrement();
    }

    @Override
    public void run() {
        if(skipMonitoring) {
            return;
        }
        while(true) {
            if(queuedTasks.get() > 0) {
                logger.info("queuedTasks remaining "+ queuedTasks.get());
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            else {
                mediaOrganizer.cancel();
                return;
            }
        }
    }
}
