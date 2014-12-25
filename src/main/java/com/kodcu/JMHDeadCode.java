package com.kodcu;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by usta on 24.12.2014.
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode({Mode.AverageTime})
@State(Scope.Benchmark)
public class JMHDeadCode {

    private final int n=10;
    private final int p=2;

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    public void methodOne() {
        double result = Math.pow(n,p);
//        blackhole.consume(result);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    public double methodTwo() {
        double result = Math.pow(n,p);

        return result;
    }

    public static void main(String[] args) throws IOException, RunnerException {

        Options opt = new OptionsBuilder()
                .include(JMHDeadCode.class.getSimpleName())
                .build();

        Collection<RunResult> run = new Runner(opt).run();
    }
}
