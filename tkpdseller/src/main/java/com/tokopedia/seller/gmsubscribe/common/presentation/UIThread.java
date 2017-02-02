package com.tokopedia.seller.gmsubscribe.common.presentation;


import com.tokopedia.seller.gmsubscribe.common.domain.executor.PostExecutionThread;


import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

public class UIThread implements PostExecutionThread {

    public UIThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
