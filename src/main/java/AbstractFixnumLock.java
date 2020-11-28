import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractFixnumLock implements FixnumLock{
    private int numberOfThreads;
    private int cntFreeThreads;
    private List<Long> registeredThreads;
    private List<Integer> freeIDs;

    private static final Object syncObj = new Object();

    AbstractFixnumLock(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.cntFreeThreads = numberOfThreads;
        this.registeredThreads = new ArrayList<>();
        this.freeIDs = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            this.freeIDs.add(i);
            this.registeredThreads.add((long) -1);
        }
    }

    @Override
    public int getId() {
        for (int i = 0; i < this.numberOfThreads; ++i) {
            if (this.registeredThreads.get(i) == Thread.currentThread().getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int register() {
        synchronized (syncObj) {
            int id = getId();
            if (id == - 1) {
                if (cntFreeThreads == 0)
                    return -1;
                cntFreeThreads--;
                registeredThreads.set(freeIDs.get(0), Thread.currentThread().getId());
                freeIDs.remove(freeIDs.get(0));
            }
            return getId();
        }
    }

    @Override
    public int unregister() {
        synchronized (syncObj) {
            int id = getId();
            if (id != -1) {
                registeredThreads.set(id, (long) -1);
                freeIDs.add(id);
//                System.out.println("Unregister thread with id " + id + ".");
                return id;
            } else {
//                System.out.println("Thread with actual id " + Thread.currentThread().getId() + " is not registered.");
            }
            return -1;
        }
    }

    private void reset() {
        synchronized (syncObj) {
            registeredThreads.clear();
            this.cntFreeThreads = numberOfThreads;
            this.freeIDs.clear();
            for (int i = 0; i < numberOfThreads; i++) {
                this.freeIDs.add(i);
                this.registeredThreads.add((long) -1);
            }
        }
    }
}