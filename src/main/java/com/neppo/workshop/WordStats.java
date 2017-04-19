package com.neppo.workshop;

import java.util.Map;

public interface WordStats {

    Integer numberOfLines();

    Integer numberOfWords();

    Map<String, Integer> wordHistogram();

    void incrementWordCount(String word);

    void incrementLineCount();
}
