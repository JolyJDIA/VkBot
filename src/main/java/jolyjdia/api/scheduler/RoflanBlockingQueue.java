package jolyjdia.api.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class RoflanBlockingQueue  {
    private static final int INITIAL_CAPACITY = 16;
    private Task[] queue = new Task[INITIAL_CAPACITY];
    private int size;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    private void siftDown(Task task) {
        int k = 0;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Task c = queue[child];
            int right = child + 1;
            if (right < size && c.compareTo(queue[right]) > 0) {
                c = queue[child = right];
            }
            if (task.compareTo(c) <= 0) {
                break;
            }
            queue[k] = c;
            c.setHeapIndex(k);
            k = child;
        }
        queue[k] = task;
        task.setHeapIndex(k);
    }

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

    public final int size() {
        reentrantLock.lock();
        try {
            return size;
        } finally {
            reentrantLock.unlock();
        }
    }

    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // grow 50%
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        queue = Arrays.copyOf(queue, newCapacity);
    }

    public final void add(Task task) {
        reentrantLock.lock();
        try {
            int i = size;
            if (i >= queue.length) {
                grow();
            }
            ++size;
            if (i == 0) {
                queue[0] = task;
                task.setHeapIndex(0);
            } else {
                siftUp(i, task);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public final Task peek() {
        reentrantLock.lock();
        try {
            return queue[0];
        } finally {
            reentrantLock.unlock();
        }
    }

    public final void remove() {
        reentrantLock.lock();
        try {
            --size;
            Task replacement = queue[size];
            queue[size] = null;
            if (size != 0) {
                siftDown(replacement);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public final void remove(int i) {
        reentrantLock.lock();
        try {
            if (i <= size) {
                queue[i].setHeapIndex(-1);
                queue[i] = queue[size];
                queue[size--] = null;
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public final void remove(@NotNull Task task) {
        reentrantLock.lock();
        try {
            int i = task.getHeapIndex();
            if (i >= 0 && i < size && queue[i] == task) {
                queue[i].setHeapIndex(-1);
                queue[i] = queue[size];
                queue[size--] = null;
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public final void setNexRun(long newTime) {
        reentrantLock.lock();
        try {
            queue[0].setNextRun(newTime);
            siftDown(queue[0]);
        } finally {
            reentrantLock.unlock();
        }
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    public final void clear() {
        reentrantLock.lock();
        try {
            for (int i = 0; i < size; ++i) {
                queue[i].setHeapIndex(-1);
                queue[i] = null;
            }
            size = 0;
        } finally {
            reentrantLock.unlock();
        }
    }
}