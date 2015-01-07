package com.kodcu;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.StandardOpenOption.*;

/**
 * Created by usta on 26.12.2014.
 */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({Mode.Throughput})
public class RandomBenchmark {

    private static final int START = 1;
    private static final int END = 2_000_000;
    private static final int ITERATION_COUNT = 10;
    private static final int WARMUP_COUNT = 10;
    private static final int FORK_COUNT = 3;


    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
    public void classicRandom(Blackhole hole) {

        int result = new Random().nextInt(END) + START;

        hole.consume(result);
    }

    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
    public void threadLocalRandom(Blackhole hole) {

        int result = ThreadLocalRandom.current().nextInt(START,END);

        hole.consume(result);
    }

}