package com.github.swamim.media.organizer.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UnManagedExecutorService implements ExecutorService {

    private final ExecutorService wrapped;
    private volatile boolean isShutdown;

    public UnManagedExecutorService(ExecutorService wrapped) {
        this.wrapped = wrapped;
    }


    @Override
    public void shutdown() {
        isShutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown = true;
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return isShutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return isShutdown;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return wrapped.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return wrapped.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return wrapped.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return wrapped.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return wrapped.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return wrapped.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return wrapped.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        wrapped.execute(command);
    }
}
