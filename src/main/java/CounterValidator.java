import java.util.ArrayList;
import java.util.Random;

public class CounterValidator {

    private AbstractFixnumLock lock;
    private int numberOfThreads;
    private Counter counter;

    public CounterValidator(AbstractFixnumLock lock, int numberOfThreads) {
        this.lock = lock;
        this.numberOfThreads = numberOfThreads;
        this.counter = new Counter();
    }

    private ArrayList<Thread> generateArrayOfThreads() {
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
            threads.add(new Thread(new CounterThread(counter, lock, isInc, 2000)));
        }
        return threads;
    }

    public boolean runValidation() throws InterruptedException {
        lock.reset();
        System.out.println("The initial counter value is 0. Number of threads is " + (numberOfThreads) + ". " +
                "After all manipulations the counter value must be zero.");
        ArrayList<Thread> counterThreads = generateArrayOfThreads();
        for (var thread : counterThreads)
            thread.start();
        for (var thread : counterThreads)
            thread.join();

        System.out.println("After all operations counter value is: " + counter.getValue());
        return counter.getValue() == 0;
    }
}