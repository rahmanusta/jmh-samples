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
    private static final int ITERATION_COUNT = 5;
    private static final int WARMUP_COUNT = 5;
    private static final int FORK_COUNT = 3;
    private static final int THREAD_COUNT = 32;


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

    public static void main(String[] args) throws RunnerException, IOException {

        List<Integer> collect = IntStream.rangeClosed(1, THREAD_COUNT)
                .boxed().collect(Collectors.toList());

        List<Collection<RunResult>> results = new LinkedList<>();

        for (Integer t : collect) {
            Options opt = new OptionsBuilder()
                    .include(RandomBenchmark.class.getSimpleName())
                    .output("random-" + t + "-output.txt")
                    .threads(t)
                    .build();

            Collection<RunResult> run = new Runner(opt).run();

            results.add(run);

        }

        for (Collection<RunResult> result : results) {
            for (RunResult runResult : result) {
                Result primaryResult = runResult.getPrimaryResult();

                String th = "Th: " + runResult.getParams().getThreads() + " - " + primaryResult.getLabel() + " - " + primaryResult.getScore();
                Files.write(Paths.get("./random-total-output.txt"),th.getBytes(Charset.forName("UTF-8")), APPEND, WRITE, CREATE);
                System.out.println(th);
            }
        }


        List<RunResult> collect1 = results.stream().flatMap(Collection::stream).collect(Collectors.toList());

        List<String> connPut = collect1.stream().filter(r -> r.getPrimaryResult().getLabel().equals("classicRandom"))
                .map(r -> r.getPrimaryResult().getScore()).map(String::valueOf).collect(Collectors.toList());

        String connPutJoin = String.join(",", connPut);

        System.out.println(connPutJoin);

        Files.write(Paths.get("./random-total-output.txt"),connPutJoin.concat("\n").getBytes(Charset.forName("UTF-8")), APPEND, WRITE, CREATE);

        List<String> syncPut = collect1.stream().filter(r -> r.getPrimaryResult().getLabel().equals("threadLocalRandom"))
                .map(r -> r.getPrimaryResult().getScore()).map(String::valueOf).collect(Collectors.toList());

        String syncPutJoin = String.join(",", syncPut);

        System.out.println(syncPutJoin);
        Files.write(Paths.get("./random-total-output.txt"),syncPutJoin.concat("\n").getBytes(Charset.forName("UTF-8")), APPEND, WRITE, CREATE);


    }


}