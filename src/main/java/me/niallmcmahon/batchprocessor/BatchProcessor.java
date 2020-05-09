package me.niallmcmahon.batchprocessor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class BatchProcessor {

    private int maxBatchSize;
    private int numberOfWorkers;

    private static final int DEFAULT_MAX_BATCH_SIZE = 100;

    public BatchProcessor() {
        maxBatchSize = DEFAULT_MAX_BATCH_SIZE;
        numberOfWorkers = Runtime.getRuntime().availableProcessors();
    }

    public BatchProcessor(int maxBatchSize, int numberOfWorkers) {
        if(maxBatchSize < 1) {
            throw new IllegalArgumentException("Max batch size must be greater than 0");
        }
        this.maxBatchSize = maxBatchSize;
        this.numberOfWorkers = numberOfWorkers;
    }


    public <T> void process(T[] batch, Consumer<T> action) {
        ForkJoinTask<?> task = new BatchProcessingTask<>(batch, maxBatchSize, 0, batch.length, action);
        startTask(task, Runtime.getRuntime().availableProcessors());
    }

    @SafeVarargs
    public final <T> void process(Consumer<T> action, T... batch) {
        ForkJoinTask<?> task = new BatchProcessingTask<>(batch, maxBatchSize, 0, batch.length, action);
        startTask(task, Runtime.getRuntime().availableProcessors());
    }

    public <T> void process(List<T> batch, Consumer<T> action) {
        ForkJoinTask<?> task = new BatchProcessingTask<>(batch, maxBatchSize, 0, batch.size(), action);
        startTask(task, Runtime.getRuntime().availableProcessors());
    }

    private void startTask(ForkJoinTask<?> task, int numberOfWorkers) {
        ForkJoinPool pool = new ForkJoinPool(numberOfWorkers);
        pool.invoke(task);
    }

    public void setMaxBatchSize(int maxBatchSize) {
        if(maxBatchSize < 1) {
            throw new IllegalArgumentException("Max batch size must be greater than 0");
        }
        this.maxBatchSize = maxBatchSize;
    }
}
