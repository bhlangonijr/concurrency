package com.neppo.workshop.stats;

import com.neppo.workshop.WordStats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concurrent implementation for WordStats - more efficient
 */
public class WordStatsAlmostRight implements WordStats {

    public AtomicInteger lines = new AtomicInteger(0);

    public AtomicInteger words = new AtomicInteger(0);

    public Map<String, Integer> histogram = new ConcurrentHashMap<>();

    @Override
    public Integer numberOfLines() {
        return lines.get();
    }

    @Override
    public Integer numberOfWords() {
        return words.get();
    }

    @Override
    public Map<String, Integer> wordHistogram() {

        return histogram;
    }

    @Override
    public void incrementWordCount(String word) {

        words.incrementAndGet();
        Integer counter = histogram.get(word);
        if (counter == null) {
            counter = 1;
        } else {
            counter++;
        }
        histogram.put(word, counter);
    }

    @Override
    public void incrementLineCount() {

        lines.incrementAndGet();
    }
}