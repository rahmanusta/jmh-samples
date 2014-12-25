/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.kodcu;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.Throughput)
@State(value = Scope.Benchmark)
public class AtomicBenchmark {

   private AtomicLong atomicInteger;
   private long normalInteger ;
   private LongAdder longAdder;

    public synchronized long artir(){
        long i = ++normalInteger;
        return i;
    }

    @Setup
    public void init(){
        atomicInteger = new AtomicLong(0);
        normalInteger =0;
        longAdder=new LongAdder();
    }

    @Benchmark
    @Fork(value = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Threads(value =64)
    public long integerIncrement() {
        return artir();
    }

    @Benchmark
    @Fork(value = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Threads(value =64)
    public void atomicIncrement(Blackhole hole) {
        hole.consume(atomicInteger.incrementAndGet());
    }

    @Benchmark
    @Fork(value = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Threads(value =64)
    public void adderIncrement(Blackhole hole) {
        longAdder.increment();
    }

    public static void main(String[] args) throws IOException, RunnerException {
        Main.main(args);


    }


}
