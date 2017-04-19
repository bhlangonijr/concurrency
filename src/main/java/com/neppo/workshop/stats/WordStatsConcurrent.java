package com.neppo.workshop.stats;

import com.neppo.workshop.WordStats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Concurrent implementation for WordStats - more efficient
 */
public class WordStatsConcurrent implements WordStats {

    public AtomicInteger lines = new AtomicInteger(0);

    public AtomicInteger words = new AtomicInteger(0);

    public Map<String, AtomicInteger> histogram = new ConcurrentHashMap<>();

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

        return histogram.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e -> e.getValue().get()));
    }

    @Override
    public void incrementWordCount(String word) {

        words.incrementAndGet();
        histogram.computeIfAbsent(word, key -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public void incrementLineCount() {

        lines.incrementAndGet();
    }
}