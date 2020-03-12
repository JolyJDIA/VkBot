package scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class DelayedWorkQueue {
    private final ReentrantLock lock = new ReentrantLock();
    private Task[] queue = new Task[128];

    /**
     * The number of tasks in the priority queue.  (The tasks are stored in
     * queue[1] up to queue[size]).
     */
    private int size;

    /**
     * Returns the number of tasks currently on the queue.
     */
    int size() {
        return size;
    }

    /**
     * Adds a new task to the priority queue.
     */
    void add(Task task) {
        lock.lock();
        try {
            // Grow backing store if necessary
            if (size + 1 == queue.length)
                queue = Arrays.copyOf(queue, 2*queue.length);

            queue[++size] = task;
            fixUp(size);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Return the "head task" of the priority queue.  (The head task is an
     * task with the lowest nextExecutionTime.)
     */
    Task peek() {
        lock.lock();
        try {
            return queue[1];
        } finally {
            lock.unlock();
        }
    }
    public void remove(@NotNull Task task) {
        lock.lock();
        try {
            int i = -1;
            do {
                ++i;
            } while (!task.equals(queue[i]));
            queue[i].setHeapIndex(-1);
            Task replacement = queue[size];
            queue[size] = null;
            if (size != i) {
                fixDown(i);
                if (queue[i] == replacement) {
                    fixUp(i);
                }
            }
            --size;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Return the ith task in the priority queue, where i ranges from 1 (the
     * head task, which is returned by getMin) to the number of tasks on the
     * queue, inclusive.
     */
    Task get(int i) {
        return queue[i];
    }

    /**
     * Remove the head task from the priority queue.
     */
    void finishPoll() {
        lock.lock();
        try {
            queue[1] = queue[size];
            queue[size--] = null;  // Drop extra reference to prevent memory leak
            fixDown(1);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes the ith element from queue without regard for maintaining
     * the heap invariant.  Recall that queue is one-based, so
     * 1 <= i <= size.
     */
    void quickRemove(int i) {
        assert i <= size;

        queue[i] = queue[size];
        queue[size--] = null;  // Drop extra ref to prevent memory leak
    }

    /**
     * Sets the nextExecutionTime associated with the head task to the
     * specified value, and adjusts priority queue accordingly.
     */
    void setNexRun(long newTime) {
        lock.lock();
        try {
            queue[1].setNextRun(newTime);
            fixDown(1);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns true if the priority queue contains no elements.
     */
    boolean isEmpty() {
        return size==0;
    }

    /**
     * Removes all elements from the priority queue.
     */
    void clear() {
        // Null out task references to prevent memory leak
        for (int i=1; i<=size; i++)
            queue[i] = null;

        size = 0;
    }

    /**
     * Establishes the heap invariant (described above) assuming the heap
     * satisfies the invariant except possibly for the leaf-node indexed by k
     * (which may have a nextExecutionTime less than its parent's).
     *
     * This method functions by "promoting" queue[k] up the hierarchy
     * (by swapping it with its parent) repeatedly until queue[k]'s
     * nextExecutionTime is greater than or equal to that of its parent.
     */
    private void fixUp(int k) {
        while (k > 1) {
            int j = k >> 1;
            if (queue[j].getNextRun() <= queue[k].getNextRun())
                break;
            Task tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the subtree
     * rooted at k, which is assumed to satisfy the heap invariant except
     * possibly for node k itself (which may have a nextExecutionTime greater
     * than its children's).
     *
     * This method functions by "demoting" queue[k] down the hierarchy
     * (by swapping it with its smaller child) repeatedly until queue[k]'s
     * nextExecutionTime is less than or equal to those of its children.
     */
    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size && j > 0) {
            if (j < size &&
                    queue[j].getNextRun() > queue[j+1].getNextRun())
                j++; // j indexes smallest kid
            if (queue[k].getNextRun() <= queue[j].getNextRun())
                break;
            Task tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    void heapify() {
        for (int i = size/2; i >= 1; i--)
            fixDown(i);
    }
}
