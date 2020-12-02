package com;

import com.Counters.*;
import com.Fixnum.BakeryLock;
import com.Fixnum.DeckerLock;
import com.Fixnum.FixnumLock;
import com.SleepWakeup.DeadLockSimulator;

import java.util.ArrayList;
import java.util.Random;

public class Demonstration {

    private int numberOfThreads;
    private Counter counter;

    public Demonstration() {
        this.counter = new Counter();
    }

    public void task(int i) throws InterruptedException {
        switch (i) {
            case 0 -> demoTask1();
            case 1 -> demoTask2();
            case 2 -> demoTask3();
            case 3 -> demoTask4();
            case 4 -> demoTask5();
            case 5 -> demoTask6();
        }
    }

    public void demoTask1() throws InterruptedException {
        demonstrateRaceCondition();
        demonstrateDekkerSolution();
    }

    public void demoTask2(){
        System.out.println("Not quiet implemented yet...");
    }

    public void demoTask3() throws InterruptedException {
        System.out.println("\n\n-----------------------------------------");
        System.out.println("com.Demonstration of Bakery Lock solution of race condition");
        System.out.println("The initial counter value is 0. Number of threads is " + (numberOfThreads) + ". " +
                "After all manipulations the counter value must be zero.");
        numberOfThreads = 2;
        ArrayList<Thread> counterThreads = generateArrayOfThreads(new BakeryLock(2));
        for (var thread : counterThreads)
            thread.start();
        for (var thread : counterThreads)
            thread.join();

        System.out.println("After all operations counter value is: " + counter.getValue());
        counter.setValue(0);
    }

    public void demoTask4() throws InterruptedException {
        demontrateDeadLock();
    }

    public void demoTask5(){
        System.out.println("Not quiet implemented yet...");
    }

    public void demoTask6(){
        System.out.println("Not quiet implemented yet...");
    }

    private ArrayList<Thread> generateArrayOfThreads(FixnumLock lock) {
        ArrayList<Thread> threads = new ArrayList<>();
        int numInc = numberOfThreads / 2;
        int numDec = numberOfThreads / 2;
        Random random = new Random();
        for (int i = 0; i < numberOfThreads; i++){
            boolean isInc = (numInc != 0) && ((numDec == 0) || random.nextBoolean());
            if (isInc)
                numInc--;
            else
                numDec--;
            if(lock != null){
                threads.add(new Thread(new CounterThread(counter, lock, isInc, 20000)));
            }
            else{
                threads.add(new Thread(new NoLockCounterThread(counter, isInc, 20000)));
            }

        }
        return threads;
    }

// Task 1
    public void demonstrateRaceCondition() throws InterruptedException {
        System.out.println("\n\n-----------------------------------------");
        System.out.println("Demonstration of race condition");
        numberOfThreads = 2;
        System.out.println("The initial counter value is " + counter.getValue() + ". Number of threads is " + (numberOfThreads) + ". " +
                "After all manipulations the counter value must be zero.");

        ArrayList<Thread> counterThreads = generateArrayOfThreads(null);
        for (var thread : counterThreads)
            thread.start();
        for (var thread : counterThreads)
            thread.join();

        System.out.println("After all operations counter value is: " + counter.getValue());
        counter.setValue(0);
    }

    public void demonstrateDekkerSolution() throws InterruptedException {
        System.out.println("\n\n-----------------------------------------");
        System.out.println("Demonstration of Dekker solution of race condition");
        System.out.println("The initial counter value is 0. Number of threads is " + (numberOfThreads) + ". " +
                "After all manipulations the counter value must be zero.");
        numberOfThreads = 2;
        ArrayList<Thread> counterThreads = generateArrayOfThreads(new DeckerLock(2));
        for (var thread : counterThreads)
            thread.start();
        for (var thread : counterThreads)
            thread.join();

        System.out.println("(Dekker) After all operations counter value is: " + counter.getValue());
        counter.setValue(0);
    }

    // Task 4
    public void demontrateDeadLock() throws InterruptedException {
        System.out.println("\n\n-----------------------------------------");
        System.out.println("Demonstration of deadlock");
        DeadLockSimulator dls = new DeadLockSimulator();

        // create thread for producer to produce new element to the list
        Thread producerThread = new Thread(() -> {
            try {
                dls.produce();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // create thread for consumer to consume elements created by the producer
        Thread consumerThread = new Thread(() -> {
            try {
                dls.consume();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }

}
