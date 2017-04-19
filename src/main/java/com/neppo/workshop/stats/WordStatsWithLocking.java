package com.neppo.workshop.stats;

import com.neppo.workshop.WordStats;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Read/Write locks - thread-safe, but not efficient
 */
public class WordStatsWithLocking implements WordStats {

    public Integer lines = 0;

    public Integer words = 0;

    public Map<String, Integer> histogram = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Integer numberOfLines() {
        return lines;
    }

    @Override
    public Integer numberOfWords() {
        return words;
    }

    @Override
    public Map<String, Integer> wordHistogram() {
        return histogram;
    }

    @Override
    public void incrementWordCount(String word) {
        lock.writeLock().lock();
        try {
            words++;
            Integer counter = histogram.get(word);
            if (counter == null) {
                counter = 1;
            } else {
                counter++;
            }
            histogram.put(word, counter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void incrementLineCount() {
        lock.writeLock().lock();
        try {
            lines++;
        } finally {
            lock.writeLock().unlock();
        }
    }
}