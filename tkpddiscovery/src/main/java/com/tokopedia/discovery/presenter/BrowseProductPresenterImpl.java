package com.tokopedia.discovery.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nakama on 23/03/17.
 */

public class BrowseProductPresenterImpl implements BrowseProductPresenter {

    LocalCacheHandler localCacheHandler;
    private Subscription subscription;

    public BrowseProductPresenterImpl(Context context) {
        localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEFAULT_GRID_SETTINGS);
    }

    @Override
    public void retrieveLastGridConfig(String departmentId,
                                       DiscoveryInteractorImpl.GetGridConfigCallback callback) {

        getLastGridConfig(departmentId, callback);
    }

    @Override
    public void onGridTypeChanged(String departmentId, String gridType) {
        saveLastGridConfig(departmentId, gridType);
    }

    private void getLastGridConfig(final String rootDepartmentId, final DiscoveryInteractorImpl.GetGridConfigCallback callback) {
        subscription = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String config = localCacheHandler.getString(rootDepartmentId);
                        subscriber.onNext(config);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String config) {
                        callback.onSuccess(config);
                    }
                });
    }

    private void saveLastGridConfig(String departmentId, String gridType) {
        localCacheHandler.putString(departmentId, gridType);
        localCacheHandler.applyEditor();
    }

    @Override
    public void finish() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
