package com.kodcu;

/**
 * Created by usta on 23.01.2015.
 */
public class Counter {

    private long count = 0;

    public Counter() {
    }

    public Counter(long count) {
        this.count = count;
    }

    public synchronized void increment(){ // synchronized
        count++;
    }

    public synchronized void decrement(){ // synchronized
        count--;
    }

    public synchronized Long getCount(){
        return count;
    }

}
