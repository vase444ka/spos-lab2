package com.Counters;

import java.util.concurrent.locks.Lock;


public class CounterThread implements Runnable {
    volatile Counter counter;
    volatile Lock lock;
    boolean isIncrement;
    int cntIterations;

    public CounterThread(Counter counter, Lock lock, boolean increment, int cntIterations) {
        this.counter = counter;
        this.lock = lock;
        this.isIncrement = increment;
        this.cntIterations = cntIterations;
    }

    public void run() {
        for (int i = 0; i < cntIterations; i++) {
            if (lock != null)
                lock.lock();
            if (isIncrement)
                counter.increment();
            else
                counter.decrement();
            if (lock != null)
                lock.unlock();
           // System.out.println("(Lock) com.Counters.Counter value is " + counter.toString());
        }
    }
}