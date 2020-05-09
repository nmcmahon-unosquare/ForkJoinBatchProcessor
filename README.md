# Batch Processor
An abstraction for Java's fork-join framework to allow for easy batch processing. This allows the user to submit a collection of objects, a max batch size and the number of worker threads they want to allocate for batch processing. The processor will then split the collection up into batches and process each batch concurrently limited only by the number of workers.

## How To Build
Clone the repository, ensure you have [Apache Maven](https://maven.apache.org/) installed and, from project root, run the following command

`mvn clean install`

## How To Test
Run the following command:

`mvn test`

## Usage
```java
// Define a collection of objects
List<TestObject> testObjects = Arrays.asList(
        new TestObject(0),
        new TestObject(50),
        new TestObject(-542),
        new TestObject(10000)
);

// Define what work should be performed on each object
Consumer<TestObject> testObjectConsumer = testObject -> {
    int oldValue = testObject.getValue();
    testObject.setValue(oldValue * 2);
};

// Initialise new batch processor with max batch size and number of worker threads.
// Can also use a no args constructor (default max batch size is 100, default workers is the number of processors on the machine)
BatchProcessor batchProcessor = new BatchProcessor(100, 4);
batchProcessor.process(testObjects, testObjectConsumer);
```

Results generation and aggregation across batches is currently not supported. Coming soon.