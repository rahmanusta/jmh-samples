package com.kodcu;

import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * Created by usta on 07.01.2015.
 */
public class Starter {

    public static void main(String[] args) throws IOException, RunnerException {

        System.out.println("Started");

        AtomicBenchmark.main(args);
        RandomBenchmark.main(args);

        Runtime.getRuntime().exec("sudo halt");


    }
}
