package com.tokopedia.seller.base.data.source.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataListCacheManager {

    private static final String PREF_NAME = "CACHE_DATA_LIST";

    private SharedPreferences sharedPrefs;

    protected abstract String getPrefKeyName();

    protected abstract long getExpiredTimeInSec();

    public DataListCacheManager(@ApplicationContext Context context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Observable<Boolean> isExpired() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(sharedPrefs.getLong(getPrefKeyName(), 0) < System.currentTimeMillis());
            }
        });
    }

    public Observable<Boolean> updateExpiredTime() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                sharedPrefs.edit().putLong(getPrefKeyName(), System.currentTimeMillis() + getExpiredTimeInSec()).apply();
                subscriber.onNext(true);
            }
        });
    }
}