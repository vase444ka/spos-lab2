package com.SleepWakeup;

import java.util.LinkedList;
import java.util.Queue;

public class DeadLockSimulator {
    static private boolean DeadLockAssist = true;

    Queue<Integer> list = new LinkedList<>();
    final static int capacity = 5;

    public void produce() throws InterruptedException
    {
        int value = 1;
        Thread.sleep(1000);
        while (true) {
            synchronized (this)
            {
                if (list.size() >= capacity)
                    wait();

                System.out.println("Producer produced: " + value);

                list.add(value++);

                // here I'm simulating deadlock: if first element was added - make this variable false
                if(DeadLockAssist) {
                    System.out.println("First item is put into queue");
                    DeadLockAssist = false;
                }

                // notify consumer, that there is new element in the list, that it can consume
                if(list.size() == 1) {
                    System.out.println("Notify (wake up) the consumer.");
                    notify();
                }

                Thread.sleep(1000);
            }
        }
    }

    public void consume() throws InterruptedException
    {
        while (true) {
            synchronized (this)
            {
                if (list.size() == 0) {
                    // simulation of deadlock - interrupt consumer before sleep
                    if(DeadLockAssist) {
                        System.out.println("Just before calling wait(), the consumer is interrupted and the producer is resumed.");

                        wait();
                        System.out.println("Unfortunately, the consumer wasn't sleeping, and the wakeup call is lost. Next time the consumer goes to sleep it won't wake up");
                        System.out.println("We have achieved deadlock!");
                    }
                    wait();
                }

                int val = list.poll();

                System.out.println("Consumer consumed:" + val);

                // if there is new space for elements - notify producer
                if(list.size() == capacity - 1)
                    notify();

                Thread.sleep(1000);
            }
        }
    }
}
