package com.moon.core;

public interface TaskWrapper {

    void produce();

    DiscoveryTask getListTask(Object... arg);

    DiscoveryTask getDetailTask(Object... arg);
}
