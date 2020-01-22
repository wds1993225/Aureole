package com.moon.core;

public interface ListProcessor extends TaskProcessor {

    String page(String content);

    void process(String content);
}
