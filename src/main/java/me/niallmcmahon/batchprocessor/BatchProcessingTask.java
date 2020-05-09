package me.niallmcmahon.batchprocessor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class BatchProcessingTask<T> extends RecursiveAction {

    private List<T> batch;
    private int maxBatchSize;
    private int start;
    private int end;
    private Consumer<T> consumer;

    BatchProcessingTask(List<T> batch, int maxBatchSize, int start, int end, Consumer<T> consumer) {
        this.batch = batch;
        this.maxBatchSize = maxBatchSize;
        this.start = start;
        this.end = end;
        this.consumer = consumer;
    }

    BatchProcessingTask(T[] array, int maxBatchSize, int start, int end, Consumer<T> consumer) {
        this(Arrays.asList(array), maxBatchSize, start, end, consumer);
    }

    @Override
    protected void compute() {
        int batchSize = end - start;
        if(batchSize <= maxBatchSize) {
            processBatch();
        }
        else {
            int middle = start + (batchSize/2);
            invokeAll(new BatchProcessingTask<>(batch, maxBatchSize, start, middle, consumer), new BatchProcessingTask<>(batch, maxBatchSize, middle, end, consumer));
        }
    }

    private void processBatch() {
        for(int i = start; i < end; i++) {
            try {
                consumer.accept(batch.get(i));
            }
            catch (Exception ex) {
                System.out.printf("Exception when trying to process item at index %d. Exception: %s Message: %s\n", i, ex.getClass().getName(), ex.getMessage());
            }
        }
    }
}
