/*
package scheduler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RoflanBlockingQueue extends AbstractQueue<Task> implements BlockingQueue<Task> {
    private static final int INITIAL_CAPACITY = 16;
    private Task[] queue = new Task[INITIAL_CAPACITY];
    private final ReentrantLock lock = new ReentrantLock();
    private int size;
    @Nullable private Thread leader;
    private final Condition available = lock.newCondition();

    private void siftUp(int k, Task key) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Task e = queue[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            queue[k] = e;
            e.setHeapIndex(k);
            k = parent;
        }
        queue[k] = key;
        key.setHeapIndex(k);
    }
    
    private void siftDown(int k, Task key) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Task c = queue[child];
            int right = child + 1;
            if (right < size && c.compareTo(queue[right]) > 0) {
                c = queue[child = right];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            queue[k] = c;
            c.setHeapIndex(k);
            k = child;
        }
        queue[k] = key;
        key.setHeapIndex(k);
    }
    
    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // grow 50%
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        queue = Arrays.copyOf(queue, newCapacity);
    }
    
    private int indexOf(Object x) {
        if (x != null) {
            if (x instanceof Task) {
                int i = ((Task) x).getHeapIndex();
                if (i >= 0 && i < size && queue[i] == x) {
                    return i;
                }
            }
        }
        return -1;
    }
    @Override
    public final boolean contains(Object x) {
        lock.lock();
        try {
            return indexOf(x) != -1;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public final boolean remove(Object x) {
        lock.lock();
        try {
            int i = indexOf(x);
            if (i < 0) {
                return false;
            }
            queue[i].setHeapIndex(-1);
            --size;
            Task replacement = queue[size];
            queue[size] = null;
            if (size != i) {
                siftDown(i, replacement);
                if (queue[i] == replacement) {
                    siftUp(i, replacement);
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public final int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }
    @Override
    public final int remainingCapacity() {
        return Integer.MAX_VALUE;
    }
    @Override
    public final Task peek() {
        lock.lock();
        try {
            return queue[0];
        } finally {
            lock.unlock();
        }
    }
    @Override
    public final boolean offer(@NotNull Task e) {
        lock.lock();
        try {
            int i = size;
            if (i >= queue.length) {
                grow();
            }
            ++size;
            if (i == 0) {
                queue[0] = e;
                e.setHeapIndex(0);
            } else {
                siftUp(i, e);
            }
            if (queue[0] == e) {
                leader = null;
                available.signalAll();
            }
        } finally {
            lock.unlock();
        }
        return true;
    }
    @Override
    public final void put(@NotNull Task e) {
        offer(e);
    }
    @Override
    public final boolean add(@NotNull Task e) {
        return offer(e);
    }

    @Override
    public final boolean offer(Task e, long timeout, @NotNull TimeUnit unit) {
        return offer(e);
    }
    
    @NotNull
    @Contract("_ -> param1")
    private Task finishPoll(Task f) {
        queue[0].setNextRun(System.currentTimeMillis()+f.getNextRun());
        siftDown(0, queue[0]);
        return queue[0];
    }

    @Nullable
    @Override
    public final Task poll() {
        lock.lock();
        try {
            Task first = queue[0];
            //System.out.println(first.getDelay() > 0);
            return (first == null || first.getDelay() > 0) ? null : finishPoll(first);
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    @Override
    public final Task take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            for (;;) {
                Task first = queue[0];
                if (first == null) {
                    available.await();
                } else {
                    long delay = first.getDelay();
                    if (delay <= 0L) {
                        return finishPoll(first);
                    }
                    if (leader != null) {
                        available.await();
                    } else {
                        Thread thisThread = Thread.currentThread();
                        leader = thisThread;
                        try {
                            available.await(delay, TimeUnit.MILLISECONDS);
                        } finally {
                            if (leader == thisThread) {
                                leader = null;
                            }
                        }
                    }
                }
            }
        } finally {
            if (leader == null && queue[0] != null) {
                available.signalAll();
            }
            lock.unlock();
        }
    }

    @Nullable
    public final Task poll(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly();
        try {
            for (;;) {
                Task first = queue[0];
                if (first == null) {
                    if (nanos <= 0L) {
                        return null;
                    } else {
                        nanos = available.awaitNanos(nanos);
                    }
                } else {
                    long delay = TimeUnit.MILLISECONDS.toNanos(first.getDelay());
                    if (delay <= 0L) {
                        return finishPoll(first);
                    }
                    if (nanos <= 0L) {
                        return null;
                    }
                    if (nanos < delay || leader != null) {
                        nanos = available.awaitNanos(nanos);
                    } else {
                        Thread thisThread = Thread.currentThread();
                        leader = thisThread;
                        try {
                            long timeLeft = available.awaitNanos(delay);
                            nanos -= delay - timeLeft;
                        } finally {
                            if (leader == thisThread) {
                                leader = null;
                            }
                        }
                    }
                }
            }
        } finally {
            if (leader == null && queue[0] != null) {
                available.signalAll();
            }
            lock.unlock();
        }
    }

    @Override
    public final void clear() {
        lock.lock();
        try {
            for (int i = 0; i < size; i++) {
                Task t = queue[i];
                if (t != null) {
                    queue[i] = null;
                    t.setHeapIndex(-1);
                }
            }
            size = 0;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public final int drainTo(@NotNull Collection<? super Task> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }
    @Override
    public final int drainTo(@NotNull Collection<? super Task> c, int maxElements) {
        Objects.requireNonNull(c);
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        lock.lock();
        try {
            int n = 0;
            for (Task first; n < maxElements && (first = queue[0]) != null && first.getDelay() <= 0;) {
                c.add(first);
                finishPoll(first);
                ++n;
            }
            return n;
        } finally {
            lock.unlock();
        }
    }
    @NotNull
    @Override
    public final Object[] toArray() {
        lock.lock();
        try {
            return Arrays.copyOf(queue, size, Object[].class);
        } finally {
            lock.unlock();
        }
    }
    
    @NotNull
    @Override
    public final <T> T[] toArray(@NotNull T[] a) {
        lock.lock();
        try {
            if (a.length < size) {
                return (T[]) Arrays.copyOf(queue, size, a.getClass());
            }
            System.arraycopy(queue, 0, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        } finally {
            lock.unlock();
        }
    }
    @NotNull
    @Override
    public final Iterator<Task> iterator() {
        lock.lock();
        try {
            return new Itr(this, Arrays.copyOf(queue, size));
        } finally {
            lock.unlock();
        }
    }
    
    private static class Itr implements Iterator<Task> {
        final Task[] array;
        final RoflanBlockingQueue runnables;
        int cursor;
        int lastRet = -1;

        Itr(RoflanBlockingQueue queue, Task[] array) {
            this.runnables = queue;
            this.array = array;
        }
        @Override
        public final boolean hasNext() {
            return cursor < array.length;
        }
        @Override
        public final Task next() {
            if (cursor >= array.length) {
                throw new NoSuchElementException();
            }
            return array[lastRet = cursor++];
        }
        @Override
        public final void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            runnables.remove(array[lastRet]);
            lastRet = -1;
        }
    }
}*/
