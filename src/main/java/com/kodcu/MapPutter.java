package com.kodcu;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by usta on 22.12.2014.
 *
 * Measures ConcurrentHashMap and Synchronized Map's Throughput
 *
 */

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode({Mode.Throughput})
@State(Scope.Benchmark)
public class MapPutter {

    private Map<Integer, Integer> synchronizedMap;
    private Map<Integer, Integer> concurrentMap;

    private static final int N = 2_000_000;
    private static final int THREAD_COUNT = 32;
    private static final int ITERATION_COUNT = 10;
    private static final int WARMUP_COUNT = 10;
    private static final int FORK_COUNT = 3;

    @Setup(Level.Trial)
    public void init() {
        synchronizedMap = Collections.synchronizedMap(new HashMap<>(N));
        concurrentMap = new ConcurrentHashMap<>(N);
    }

    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
//    @Threads(value = 16)
    public Integer syncPut() {
        int key = ThreadLocalRandom.current().nextInt(1, N);
        int value = ThreadLocalRandom.current().nextInt(1, N);
        Integer result = synchronizedMap.put(key, value);

        return result;
    }

    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
//    @Threads(value = 16)
    public Integer connPut() {
        int key = ThreadLocalRandom.current().nextInt(1, N);
        int value = ThreadLocalRandom.current().nextInt(1, N);
        Integer result = concurrentMap.put(key, value);

        return result;
    }

    public static void main(String[] args) throws IOException, RunnerException {

        List<Integer> collect = IntStream.rangeClosed(1, THREAD_COUNT)
                .boxed().collect(Collectors.toList());

        List<Collection<RunResult>> results = new LinkedList<>();

        for (Integer t : collect) {
            Options opt = new OptionsBuilder()
                    .include(MapPutter.class.getSimpleName())
                    .threads(t)
                    .build();

            Collection<RunResult> run = new Runner(opt).run();

            results.add(run);

        }

        for (Collection<RunResult> result : results) {
            for (RunResult runResult : result) {
                Result primaryResult = runResult.getPrimaryResult();
                System.out.println("Th: " + runResult.getParams().getThreads() + " - " + primaryResult.getLabel() + " - " + primaryResult.getScore());
            }
        }


    }
}
