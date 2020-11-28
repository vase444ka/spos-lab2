class CounterThread implements Runnable {
    volatile Counter counter;
    volatile FixnumLock lock;
    boolean isIncrement;
    int cntIterations;

    public CounterThread(Counter counter, FixnumLock lock, boolean increment, int cntIterations) {
        this.counter = counter;
        this.lock = lock;
        this.isIncrement = increment;
        this.cntIterations = cntIterations;
    }

    public void run() {
        for (int i = 0; i < cntIterations; i++) {
            lock.lock();
            if (isIncrement)
                counter.increment();
            else
                counter.decrement();
            lock.unlock();
            System.out.println("Counter value is " + counter.toString());
        }
    }
}