package com.gmcc.msb.config.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class RefreshCallable implements Callable<Map<String,String>> {

    private final CountDownLatch latch;
    private RefreshTask task;

    public RefreshCallable(CountDownLatch latch,RefreshTask task){
        this.latch = latch;
        this.task=task;
    }

    @Override
    public Map<String,String> call() throws Exception {
        Map<String, String> result = new HashMap<>();
        try {
            result = task.remoteRefresh();
        }catch (Exception e){
            result.put("code",e.getMessage());
        }
        latch.countDown();
        return result;
    }
}

