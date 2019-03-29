package com.tokopedia.tkpdpdp;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

public class RxAndroidTestPlugins {

    private static RxAndroidSchedulersHook androidRxHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    public static void resetAndroidTestPlugins() {
        RxAndroidPlugins.getInstance().reset();
    }

    public static void setImmediateScheduler() {
        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(androidRxHook);
    }

}
