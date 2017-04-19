package com.neppo.workshop.stats;

import com.neppo.workshop.WordStats;

import java.util.HashMap;
import java.util.Map;

/**
 * Methods are synchronized - Thread-safe but inefficient
 */
public class WordStatsSyncMethod implements WordStats {

    public Integer lines = 0;

    public Integer words = 0;

    public Map<String, Integer> histogram = new HashMap<>();

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
    public synchronized void incrementWordCount(String word) {

        words++;
        Integer counter = histogram.get(word);
        if (counter == null) {
            counter = 1;
        } else {
            counter++;
        }
        histogram.put(word, counter);
    }

    @Override
    public synchronized void incrementLineCount() {

        lines++;
    }
}