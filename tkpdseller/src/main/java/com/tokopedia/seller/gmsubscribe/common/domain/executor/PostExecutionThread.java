package com.tokopedia.seller.gmsubscribe.common.domain.executor;

import rx.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}
