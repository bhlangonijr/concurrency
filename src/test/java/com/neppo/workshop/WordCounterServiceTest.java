package com.neppo.workshop;

import com.neppo.workshop.stats.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Text files from http://www.textfiles.com/etext/
 */

public class WordCounterServiceTest {

    final static Integer HISTOGRAM_LIMIT = 10;
    final static String BOOK_NAME = "install.log";

    static List<String> lines;
    static WordStats correctStats;

    @BeforeClass
    public static void init() throws IOException {

        String bookName = Thread.currentThread().getContextClassLoader().getResource(BOOK_NAME).getFile();
        lines = Files.lines(Paths.get(bookName), Charset.defaultCharset()).collect(Collectors.toList());
        correctStats = new WordStatsNaive();

        lines.forEach(line -> {
            String[] words = line.split(" ");
            correctStats.incrementLineCount();
            for (String word : words) {
                correctStats.incrementWordCount(word);
            }
        });

        System.out.println("Reference: " + WordStatsUtil.wordStatsToString(correctStats, 10));
        System.out.println("=============================================================================");
    }

    public static void testWordCounterInParallel(WordStats wordStats, List<String> lines) {

        long init = System.currentTimeMillis();
        lines.parallelStream().forEach(line -> {
            String[] words = line.split(" ");
            wordStats.incrementLineCount();
            for (String word : words) {
                wordStats.incrementWordCount(word);
            }
        });

        System.out.println("WordStats = " + WordStatsUtil.wordStatsToString(wordStats, HISTOGRAM_LIMIT));
        System.out.println("Time spent: " + (System.currentTimeMillis() - init));
        System.out.println("==========================================================================");
    }

    @Test
    public void testNaiveAndAsync() throws IOException {

        WordStats stats = new WordStatsNaive();
        testWordCounterInParallel(stats, lines);
        assertFalse("should not match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testNaive2AndAsync() throws IOException {

        WordStats stats = new WordStatsNaive2();
        testWordCounterInParallel(stats, lines);
        assertFalse("should not match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testWithLockingAndAsync() throws IOException {

        WordStats stats = new WordStatsWithLocking();
        testWordCounterInParallel(stats, lines);
        assertTrue("should match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testSyncBlocksAndAsync() throws IOException {

        WordStats stats = new WordStatsSyncBlock();
        testWordCounterInParallel(stats, lines);
        assertTrue("should match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testSyncMethodAndAsync() throws IOException {

        WordStats stats = new WordStatsSyncMethod();
        testWordCounterInParallel(stats, lines);
        assertTrue("should match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testAlmostRightAndAsync() throws IOException {

        WordStats stats = new WordStatsAlmostRight();
        testWordCounterInParallel(stats, lines);
        assertTrue("should match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

    @Test
    public void testConcurrentAndAsync() throws IOException {

        WordStats stats = new WordStatsConcurrent();
        testWordCounterInParallel(stats, lines);
        assertTrue("should match", WordStatsUtil.compareWorldStats(correctStats, stats));
    }

}