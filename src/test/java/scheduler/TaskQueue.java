package scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {
    private Task[] queue = new Task[16];
    private int size;
    private final ReentrantLock lock = new ReentrantLock();

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

    private void siftUp(Task key) {
        int k = size;
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
                siftUp(task);
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

    public final void remove() {
        lock.lock();
        try {
            --size;
            Task replacement = queue[size];
            queue[size] = null;
            if (size != 0) {
                siftDown(replacement);
            }
        } finally {
            lock.unlock();
        }
    }

    public final void remove(int i) {
        lock.lock();
        try {
            if (i < size) {
                queue[i].setHeapIndex(-1);
                queue[i] = queue[size];
                queue[size--] = null;
            }
        } finally {
            lock.unlock();
        }
    }

    public final void remove(@NotNull Task task) {
        lock.lock();
        try {
            int i = task.getHeapIndex();
            if (i > 0 && i < size && queue[i] == task) {
                //queue[i].setHeapIndex(-1);
                queue[i] = queue[size];
                queue[size-1] = null;
            }
            --size;
        } finally {
            lock.unlock();
        }
        System.out.println(Arrays.toString(queue));
    }

    public final void setNexRun(long newTime) {
        lock.lock();
        try {
            queue[0].setNextRun(newTime);
            siftDown(queue[0]);
        } finally {
            lock.unlock();
        }
    }

    public final boolean isEmpty() {
        return size() == 0;
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