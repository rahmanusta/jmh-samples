package com.kodcu;

import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;

/**
 * Created by usta on 07.01.2015.
 */
public class Starter {

    public static void main(String[] args) throws IOException, RunnerException {

        AtomicBenchmark.main(args);
        RandomBenchmark.main(args);

        Runtime.getRuntime().exec("sudo halt");

    }
}
