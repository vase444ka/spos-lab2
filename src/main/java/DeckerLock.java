import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;

public class DeckerLock extends AbstractFixnumLock{

    private AtomicLong turn = new AtomicLong();
    AtomicBoolean[] a = new AtomicBoolean[]{new AtomicBoolean(false), new AtomicBoolean(false)};
    private ArrayList<AtomicBoolean> flags = new ArrayList<>(Arrays.asList(a));
    @Override
    public void lock() {
        register();
        int id = getId();
        int oppositeThreadId = (id == 0 ?  1 : 0);
        if(id == -1)
            return;
        flags.get(id).set(true);
        while (flags.get(oppositeThreadId).get()) {
            if (turn.get() == oppositeThreadId) {
                flags.get(id).set(false);
                while(turn.get() == oppositeThreadId);

                flags.get(id).set(true);
            }
        }
    }

    @Override
    public void unlock() {
        int id = getId();
        if(id == -1)
            return;
        int oppositeThreadId = (id == 0 ?  1 : 0);
        turn.set(oppositeThreadId);
        flags.get(id).set(false);
    }

    @Override
    public boolean tryLock() {
        int id = getId();
        int oppositeThreadId = (id == 0 ?  1 : 0);
        if(id == -1)
            return false;
        flags.get(id).set(true);
        while (flags.get(oppositeThreadId).get()) {
            if (turn.get() == (long)oppositeThreadId) {
                flags.get(id).set(false);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        int id = getId();
        int oppositeThreadId = (id == 0 ?  1 : 0);
        if(id == -1)
            return false;
        var currentTime = System.currentTimeMillis();
        long endTime = currentTime + unit.toMillis(time);

        flags.get(id).set(true);
        while (flags.get(oppositeThreadId).get()) {
            if (turn.get() == oppositeThreadId) {
                flags.get(id).set(false);

                while (turn.get() == oppositeThreadId) {
                    if (currentTime == endTime)
                        return false;
                    currentTime++;
                }

                flags.get(id).set(true);
            }
        }

        return true;
    }

    @Override
    public void lockInterruptibly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }


    DeckerLock(int numberOfThreads) {
        super(numberOfThreads);
    }


}
