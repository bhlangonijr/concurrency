package com.neppo.workshop.stats;

import com.neppo.workshop.WordStats;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Using sync blocks - Thread-safe but not efficient
 */
public class WordStatsSyncBlock implements WordStats {

    public Integer lines = 0;

    public Integer words = 0;

    public Map<String, Integer> histogram = new HashMap<>();

    private final Object monitor = new Object();

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

        synchronized (monitor) {
            words++;
            Integer counter = histogram.get(word);
            if (counter == null) {
                counter = 1;
            } else {
                counter++;
            }
            histogram.put(word, counter);
        }
    }

    @Override
    public void incrementLineCount() {

        synchronized (monitor) {
            lines++;
        }
    }
}