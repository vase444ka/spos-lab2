package com.Locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ImprovedBakeryLock implements Lock{
    private AtomicLong ticketCounter = new AtomicLong(0);
    private AtomicLong unlockedCounter = new AtomicLong(1);


    @Override
    public void lock(){
        final long ticket = ticketCounter.getAndIncrement() + 1;

        if (ticket < 0) {
            throw new IllegalArgumentException("ticket out of range");
        }

        while(unlockedCounter.get() != ticket){
            Thread.yield();
        }
    }

    @Override
    public void unlock(){
        unlockedCounter.getAndIncrement();
    }

    @Override
    public Condition newCondition() { return null; }

    @Override
    public boolean tryLock() {
        final long ticket = ticketCounter.get() + 1;

        if (ticket < 0) {
            throw new IllegalArgumentException("ticket out of range");
        }

        if (unlockedCounter.get() != ticket){
            return false;
        }

        long expected = ticket - 1;
        return unlockedCounter.compareAndSet(expected, ticket);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException{}

}
