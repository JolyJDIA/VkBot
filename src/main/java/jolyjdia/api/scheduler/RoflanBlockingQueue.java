package jolyjdia.api.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class RoflanBlockingQueue {
    private Task[] queue = new Task[16];
    private int size;
    private final ReentrantLock lock = new ReentrantLock();

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

    public final int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); //grow 50%
        if (newCapacity < 0) {//переполнение
            newCapacity = Integer.MAX_VALUE - 8;
        }
        queue = Arrays.copyOf(queue, newCapacity);
    }

    public final void add(Task task) {
        lock.lock();
        try {
            if (size >= queue.length) {
                grow();
            }
            if (size == 0) {
                queue[0] = task;
                task.setHeapIndex(0);
            } else {
                siftUp(size, task);
            }
            ++size;
        } finally {
            lock.unlock();
        }
    }

    public final Task peek() {
        lock.lock();
        try {
            return queue[0];
        } finally {
            lock.unlock();
        }
    }

    public final void finishPoll() {
        lock.lock();
        try {
            queue[0].setHeapIndex(-1);
            --size;
            Task x = queue[size];
            queue[size] = null;
            if (size != 0) {
                siftDown(0, x);
            }
        } finally {
            lock.unlock();
        }
    }

    public final void remove(@NotNull Task task) {
        lock.lock();
        try {
            int i = task.getHeapIndex();
            if (i < 0) {
                return;
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
        } finally {
            lock.unlock();
        }
    }

    public final void setNexRun(long newTime) {
        lock.lock();
        try {
            queue[0].setNextRun(newTime);
            siftDown(0, queue[0]);
        } finally {
            lock.unlock();
        }
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    public final boolean contains(@NotNull Task x) {
        lock.lock();
        try {
            return x.getHeapIndex() != -1;
        } finally {
            lock.unlock();
        }
    }

    public final void clear() {
        lock.lock();
        try {
            for (int i = 0; i < size; ++i) {
                queue[i].setHeapIndex(-1);
                queue[i] = null;
            }
            size = 0;
        } finally {
            lock.unlock();
        }
    }
}