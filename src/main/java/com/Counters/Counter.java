package com.Counters;

public class Counter {
    private Integer value = 0;

    public Counter() {}

    public void increment() {
        ++value;
    }

    public void decrement() {
        --value;
    }

    public String toString() {
        return value.toString();
    }

    public Integer getValue(){
        return value;
    }

    public void setValue(Integer newValue){
        value = newValue;
    }


}
