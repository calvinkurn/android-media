package com.tokopedia.seller.common.usecase;

import rx.Scheduler;

/**
 * Use Usecase from tkpd usecase
 */
@Deprecated
public interface PostExecutionThread {
    Scheduler getScheduler();
}
