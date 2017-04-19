package com.neppo.workshop;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WordStatsUtil {

    public static String wordHistogramToString(Map<String, Integer> histogram, Integer limit) {

        if (histogram != null) {
            return histogram.entrySet()
                    .stream()
                    .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                    .limit(limit)
                    .map(entry -> entry.getKey() + " = " + entry.getValue())
                    .collect(Collectors.joining("\n"));
        } else {
            return "";
        }
    }

    public static String wordStatsToString(WordStats wordStats, Integer histogramLimit) {

        return wordStats.getClass().getCanonicalName() + "{" +
                "  lines=" + wordStats.numberOfLines() + "\n" +
                ", words=" + wordStats.numberOfWords() + "\n" +
                ", histogram=" + WordStatsUtil.wordHistogramToString(wordStats.wordHistogram(), histogramLimit) +
                '}';
    }

    public static boolean compareWorldStats(WordStats o1, WordStats o2) {

        return Objects.equals(o1.numberOfLines(), o2.numberOfLines()) &&
                Objects.equals(o1.numberOfWords(), o2.numberOfWords()) &&
                Objects.equals(o1.wordHistogram(), o1.wordHistogram());
    }

}
