package com.neppo.workshop;

import java.util.List;

@FunctionalInterface
public interface WordStatsProcessor {

    WordStats process(List<String> lines);
}
