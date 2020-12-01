class NoLockCounterThread implements Runnable {
    volatile Counter counter;
    boolean isIncrement;
    int cntIterations;

    public NoLockCounterThread(Counter counter,boolean increment, int cntIterations) {
        this.counter = counter;
        this.isIncrement = increment;
        this.cntIterations = cntIterations;
    }

    public void run() {
        for (int i = 0; i < cntIterations; i++) {
            if (isIncrement)
                counter.increment();
            else
                counter.decrement();
          //  System.out.println("(No lock) Counter value is " + counter.toString());
        }
    }
}