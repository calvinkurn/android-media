package com.tokopedia.usecase.executor;

import rx.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}
