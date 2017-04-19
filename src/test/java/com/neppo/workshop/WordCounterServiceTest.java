package com.neppo.workshop;

import com.neppo.workshop.stats.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Text files from http://www.textfiles.com/etext/
 */

public class WordCounterServiceTest {

    final static Integer HISTOGRAM_LIMIT = 10;
    final static Integer REPEAT_TESTS = 10;

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

    public static void testWordCounterInParallel(Supplier<WordStats> wordStatsSupplier, List<String> lines, Integer repeat, Boolean shouldMatch) {

        long init = System.currentTimeMillis();
        IntStream.rangeClosed(1, repeat).forEach( i -> {
            WordStats wordStats = wordStatsSupplier.get();
            lines.parallelStream().forEach(line -> {
                String[] words = line.split(" ");
                wordStats.incrementLineCount();
                for (String word : words) {
                    wordStats.incrementWordCount(word);
                }
            });
            System.out.println("WordStats = " + WordStatsUtil.wordStatsToString(wordStats, HISTOGRAM_LIMIT));
            if (shouldMatch) {
                assertTrue("should match", WordStatsUtil.compareWordStats(correctStats, wordStats));
            } else {
                assertFalse("should not match", WordStatsUtil.compareWordStats(correctStats, wordStats));
            }
        });
        System.out.println("Time spent (avg): " + (System.currentTimeMillis() - init)/repeat);
        System.out.println("==========================================================================");
    }

    @Test
    public void testNaiveAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsNaive::new, lines, REPEAT_TESTS, false);
    }

    @Test
    public void testNaive2AndAsync() throws IOException {

        testWordCounterInParallel(WordStatsNaive2::new, lines, REPEAT_TESTS, false);
    }

    @Test
    public void testWithLockingAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsWithLocking::new, lines, REPEAT_TESTS, true);

    }

    @Test
    public void testSyncBlocksAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsSyncBlock::new, lines, REPEAT_TESTS, true);
    }

    @Test
    public void testSyncMethodAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsSyncMethod::new, lines, REPEAT_TESTS, true);
    }

    @Test
    public void testAlmostRightAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsAlmostRight::new, lines, REPEAT_TESTS, true);
    }

    @Test
    public void testConcurrentAndAsync() throws IOException {

        testWordCounterInParallel(WordStatsConcurrent::new, lines, REPEAT_TESTS, true);
    }

}