package com.kodcu;

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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.StandardOpenOption.*;

/**
 * Created by usta on 07.01.2015.
 */
public class Starter {

    public void start(String benchmarkClass, int threadCount,String... benchmarkNames) throws Exception {

        List<Integer> threadRange = IntStream.rangeClosed(1, threadCount)
                .boxed().collect(Collectors.toList());

        List<Collection<RunResult>> results = new LinkedList<>();

        for (Integer t : threadRange) {
            Options opt = new OptionsBuilder()
                    .include(benchmarkClass)
                    .output(benchmarkClass + "-" + t + "-output.txt")
                    .threads(t)
                    .build();

            Collection<RunResult> result = new Runner(opt).run();

            results.add(result);

        }

        for (Collection<RunResult> result : results) {
            for (RunResult runResult : result) {
                Result primaryResult = runResult.getPrimaryResult();

                StringBuilder builder = new StringBuilder();
                builder.append("Thread:\t");
                builder.append(runResult.getParams().getThreads());
                builder.append("\t");
                builder.append(primaryResult.getLabel());
                builder.append("\t");
                builder.append(primaryResult.getScore());
                builder.append("\n");

                Files.write(Paths.get("./" + benchmarkClass + "-total-output.txt"), builder.toString().getBytes(Charset.forName("UTF-8")), APPEND, WRITE, CREATE);
            }
        }

        List<RunResult> allResults = results.stream().flatMap(Collection::stream).collect(Collectors.toList());

        for (String benchmarkName : benchmarkNames) {
            List<String> filteredResult = allResults.stream()
                                    .filter(r -> r.getPrimaryResult().getLabel().equals(benchmarkName))
                    .map(r -> r.getPrimaryResult().getScore())
                    .map(String::valueOf).collect(Collectors.toList());

            String joinedResult = String.join(",", filteredResult);
            joinedResult = "\n"+"\t"+benchmarkName+"\t"+joinedResult+"\n";

            Files.write(Paths.get("./"+benchmarkClass+"-total-output.txt"), joinedResult.getBytes(Charset.forName("UTF-8")), APPEND, WRITE, CREATE);
        }

    }

    public static void main(String[] args) throws Exception {

        System.out.println("Started");

        Starter starter = new Starter();
        starter.start(RandomBenchmark.class.getSimpleName(),32,"classicRandom","threadLocalRandom");
        starter.start(AtomicBenchmark.class.getSimpleName(),32,"atomicIncrement","adderIncrement");
        starter.start(MapPutter.class.getSimpleName(),32,"syncPut","connPut");

        Runtime.getRuntime().exec("sudo halt");

    }
}
