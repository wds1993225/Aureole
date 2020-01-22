package com.moon.core;

public interface DetailProcessor<T> extends TaskProcessor {


    T process(String content);

    boolean upload(T t);

}
