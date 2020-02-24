package jolyjdia.api.scheduler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

public class RoflanBlockingQueue extends AbstractQueue<Task> {
    private Task[] queue = new Task[16];
    private int size;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    @NotNull
    @Contract(" -> new")
    @Override
    public final Iterator<Task> iterator() {
        reentrantLock.lock();
        try {
            return new Itr(this, Arrays.copyOf(queue, size));
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public final boolean offer(Task e) {
        reentrantLock.lock();
        try {
            int i = size;
            if (i >= queue.length) {
                grow();
            }
            ++size;
            if (i == 0) {
                queue[0] = e;
            } else {
                siftUp(i, e);
            }
        } finally {
            reentrantLock.unlock();
        }
        return true;
    }

    @Override
    public final boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        reentrantLock.lock();
        try {
            for (Task task : queue) {
                if (o.equals(task)) {
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public final boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        reentrantLock.lock();
        try {
            for (int i = 0; i < size; ++i) {
                if (o.equals(queue[i])) {
                    int s = --size;
                    Task replacement = queue[s];
                    queue[s] = null;
                    if (s != i) {
                        siftDown(i, replacement);
                        if (queue[i] == replacement) {
                            siftUp(i, replacement);
                        }
                    }
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public final int size() {
        reentrantLock.lock();
        try {
            return size;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public final Task peek() {
        reentrantLock.lock();
        try {
            return queue[0];
        } finally {
            reentrantLock.unlock();
        }
    }

    private Task finishPoll(Task e) {
        int s = --size;
        Task x = queue[s];
        queue[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }
        return e;
    }

    @Nullable
    @Override
    public final Task poll() {
        reentrantLock.lock();
        try {
            Task first = queue[0];
            return (first == null || first.getDelay() > 0) ? null : finishPoll(first);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public final void clear() {
        reentrantLock.lock();
        try {
            for (int i = 0; i < size; i++) {
                queue[i] = null;
            }
            this.size = 0;
        } finally {
            reentrantLock.unlock();
        }
    }

    @NotNull
    @Override
    public final Object[] toArray() {
        reentrantLock.lock();
        try {
            return Arrays.copyOf(queue, size, Object[].class);
        } finally {
            reentrantLock.unlock();
        }
    }

    @NotNull
    @Override
    public final <T> T[] toArray(@NotNull T[] a) {
        reentrantLock.lock();
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
            reentrantLock.unlock();
        }
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
            k = child;
        }
        queue[k] = key;
    }

    private void grow() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // grow 50%
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        queue = Arrays.copyOf(queue, newCapacity);
    }

    private static class Itr implements Iterator<Task> {
        final RoflanBlockingQueue queue;
        final Task[] array;
        int cursor;
        int lastRet = -1;

        Itr(RoflanBlockingQueue queue, Task[] array) {
            this.array = array;
            this.queue = queue;
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
            queue.remove(array[lastRet]);
            lastRet = -1;
        }
    }
}