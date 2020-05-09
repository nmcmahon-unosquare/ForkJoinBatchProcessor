package me.niallmcmahon.batchprocessor;

import lombok.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BatchProcessorTest {


    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsSmallAndUsesVarArgs() {
        BatchProcessor batchProcessor = new BatchProcessor();
        Person testPerson1 = new Person("TestPerson1", 50);
        Person testPerson2 = new Person("TestPerson2", 60);

        batchProcessor.process((Person person) -> person.setName("Processed"), testPerson1, testPerson2);

        Assert.assertEquals("Processed", testPerson1.getName());
        Assert.assertEquals("Processed", testPerson2.getName());
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsSmallAndUsesArray() {
        BatchProcessor batchProcessor = new BatchProcessor();
        Person testPerson1 = new Person("TestPerson1", 50);
        Person testPerson2 = new Person("TestPerson2", 60);

        batchProcessor.process(new Person[] {testPerson1, testPerson2}, (Person person) -> person.setName("Processed"));

        Assert.assertEquals("Processed", testPerson1.getName());
        Assert.assertEquals("Processed", testPerson2.getName());
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsSmallAndUsesList() {
        BatchProcessor batchProcessor = new BatchProcessor();
        Person testPerson1 = new Person("TestPerson1", 50);
        Person testPerson2 = new Person("TestPerson2", 60);

        batchProcessor.process(Arrays.asList(testPerson1, testPerson2), (Person person) -> person.setName("Processed"));

        Assert.assertEquals("Processed", testPerson1.getName());
        Assert.assertEquals("Processed", testPerson2.getName());
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsLargeAndUsesVarArgs() {
        BatchProcessor batchProcessor = new BatchProcessor();
        Person[] people = getBatchOfPeople(1000).toArray(new Person[1000]);

        batchProcessor.process((Person person) -> person.setName("Processed"), people);

        for(Person person : people) {
            Assert.assertEquals("Processed", person.getName());
        }
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsLargeAndUsesArray() {
        BatchProcessor batchProcessor = new BatchProcessor();
        Person[] people = getBatchOfPeople(500).toArray(new Person[501]);

        batchProcessor.process(people, (Person person) -> person.setName("Processed"));

        for(Person person : people) {
            Assert.assertTrue(person == null || "Processed".equals(person.getName()));
        }
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsLargeAndUsesList() {
        BatchProcessor batchProcessor = new BatchProcessor();
        List<Person> people = getBatchOfPeople(2000);

        batchProcessor.process(people, (Person person) -> person.setName("Processed"));

        for(Person person : people) {
            Assert.assertEquals("Processed", person.getName());
        }
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsEmptyList() {
        BatchProcessor batchProcessor = new BatchProcessor();
        List<Person> people = new ArrayList<>();

        batchProcessor.process(people, (Person person) -> person.setName("Processed"));
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsEmptyArray() {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.process(new Person[0], (Person person) -> person.setName("Processed"));
    }

    @Test
    public void testBatchProcessorProcessesBatchWhenBatchIsEmptyVarArgs() {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.process((Person person) -> person.setName("Processed"));
    }

    @Test
    public void testBatchProcessorDefaultsNumberOfWorkersWhenNumberOfWorkersProvidedIsZero() {
        BatchProcessor batchProcessor = new BatchProcessor(100, 0);
        Person testPerson1 = new Person("TestPerson1", 50);
        Person testPerson2 = new Person("TestPerson2", 60);

        batchProcessor.process(Arrays.asList(testPerson1, testPerson2), (Person person) -> person.setName("Processed"));

        Assert.assertEquals("Processed", testPerson1.getName());
        Assert.assertEquals("Processed", testPerson2.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBatchProcessorThrowsExceptionWhenMaxBatchSizeIsZero() {
        new BatchProcessor(0, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBatchProcessorThrowsExceptionWhenTrySetMaxBatchSizeToZero() {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.setNumberOfWorkers(4);
        batchProcessor.setMaxBatchSize(0);
    }

    private List<Person> getBatchOfPeople(int numberOfPeople) {
        return Stream.generate(() -> new Person("Test Person", 50))
                .limit(numberOfPeople)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Person {
        private String name;
        private int age;
    }
}
