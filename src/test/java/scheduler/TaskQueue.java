package scheduler;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {
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
            k = child;
        }
        queue[k] = task;
    }
    private void siftUp(int k, Task key) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Task e = queue[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
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
            queue[i] = task;
            siftUp(i, task);
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

    public final void quickRemove(int i) {
        reentrantLock.lock();
        try {
            assert i <= size;
            queue[i] = queue[size];
            queue[size--] = null;
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
            for (int i = 0; i <= size; ++i) {
                queue[i] = null;
            }
            size = 0;
        } finally {
            reentrantLock.unlock();
        }
    }
}
