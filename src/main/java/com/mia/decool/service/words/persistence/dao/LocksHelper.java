package com.mia.decool.service.words.persistence.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

@Component
public class LocksHelper {

    private final Map<String, Lock> wordsLocks;
    private final ReadWriteLock fullLock;
    private final long fullReadLockTimeout;
    private final long fullWriteLockTimeout;
    private final long wordLockTimeout;


    public LocksHelper(@Value("${words.locks.timeouts.full.read:500}") long fullReadLockTimeout,
                       @Value("${words.locks.timeouts.full.write:500}") long fullWriteLockTimeout,
                       @Value("${words.locks.timeouts.work:500}") long wordLockTimeout) {
        this.fullReadLockTimeout = fullReadLockTimeout;
        this.fullWriteLockTimeout = fullWriteLockTimeout;
        this.wordLockTimeout = wordLockTimeout;
        this.wordsLocks = Collections.synchronizedMap(new HashMap<>());
        this.fullLock = new ReentrantReadWriteLock();
    }

    /**
     * Runs the supplier with a read lock on the full data structure and a write lock on the word.
     * This means that multiple threads can execute runWithLockOnWord() in parallel, but only one thread can execute it for a specific word.
     * In addition, if another thread is executing runWithLockOnDataStructure(), then all threads that are executing runWithLockOnWord() will wait for it to finish.
     *
     * @param word     the word to lock on
     * @param supplier the supplier to run
     * @param <T>      the return type of the supplier
     * @return the return value of the supplier
     * @throws InterruptedException if the thread was interrupted while waiting for the lock
     */
    public <T> T runWithLockOnWord(String word, Supplier<T> supplier) throws InterruptedException {
        try {
            boolean readLockAcquired = fullLock.readLock().tryLock(this.fullReadLockTimeout, TimeUnit.MILLISECONDS);
            if (!readLockAcquired) {
                // consider throwing a custom exception here
                throw new RuntimeException("Failed to acquire full read lock");
            }
            Lock wordLock = wordsLocks.computeIfAbsent(word, k -> new ReentrantLock());
            boolean wordWriteLockAcquired = wordLock.tryLock(this.wordLockTimeout, TimeUnit.MILLISECONDS);
            if (!wordWriteLockAcquired) {
                // consider throwing a custom exception here
                throw new RuntimeException("Failed to acquire write lock for word");
            }
            try {
                return supplier.get();
            } finally {
                wordLock.unlock();
            }
        } finally {
            fullLock.readLock().unlock();
        }
    }

    /**
     * Runs the supplier with a write lock on the full data structure.
     * This means that only one thread can execute runWithLockOnDataStructure() at a time.
     * In addition, if another thread is executing runWithLockOnWord(), then it will wait for it to finish.
     * While running the supplier, no other thread can execute runWithLockOnWord().
     *
     * @param supplier the supplier to run
     * @param <T>      the return type of the supplier
     * @return the return value of the supplier
     */
    public <T> T runWithLockOnDataStructure(Supplier<T> supplier) {
        try {
            boolean writeLockAcquired = fullLock.writeLock().tryLock(this.fullWriteLockTimeout, TimeUnit.MILLISECONDS);
            if (!writeLockAcquired) {
                // consider throwing a custom exception here
                throw new RuntimeException("Failed to acquire full write lock");
            }
            try {
                return supplier.get();
            } finally {
                fullLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            // consider throwing a custom exception here
            throw new RuntimeException(e);
        }
    }

}
