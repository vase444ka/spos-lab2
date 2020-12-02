package com.Fixnum;

import com.Fixnum.AbstractFixnumLock;
import com.Fixnum.FixnumLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;

public class BakeryLock extends AbstractFixnumLock implements FixnumLock {
    AtomicIntegerArray ticket;
    AtomicIntegerArray entering;

    public BakeryLock(int numberOfThreads) {
        super(numberOfThreads);
        this.ticket = new AtomicIntegerArray(numberOfThreads);
        this.entering = new AtomicIntegerArray(numberOfThreads);
    }

    @Override
    public void lock() {
        register();
        int tid = getId();
        entering.set(tid, 1);
        int ticket_number = getMaxTicketNumber() + 1;
        ticket.set(tid, ticket_number);
        entering.set(tid, 0);
        for (int i = 0; i < ticket.length(); ++i)
            if (i != tid) {
                while (entering.get(i) == 1)
                    Thread.yield();
                while (ticket.get(i) != 0 && ( ticket.get(tid) > ticket.get(i)  || (ticket.get(tid) == ticket.get(i) && tid > i)))
                    Thread.yield();
            }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        int tid = getId();
        ticket.set(tid, 0);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private int getMaxTicketNumber() {
        int max = 0;
        for (int i = 0; i < this.numberOfThreads; ++i) {
            int current = this.ticket.get(i);
            max = Math.max(current, max);
        }
        return max;
    }
}
