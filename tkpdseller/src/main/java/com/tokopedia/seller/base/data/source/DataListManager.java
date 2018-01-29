package com.tokopedia.seller.base.data.source;

import com.tokopedia.seller.base.data.source.cache.DataListCacheManager;
import com.tokopedia.seller.base.data.source.cloud.DataListCloudManager;
import com.tokopedia.seller.base.data.source.database.DataListDBManager;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 10/23/17.
 */

public abstract class DataListManager<T> {

    private DataListCacheManager dataListCacheManager;
    private DataListDBManager<T> dataListDBManager;
    private DataListCloudManager<T> dataListCloudManager;

    public DataListManager(DataListCacheManager dataListCacheManager, DataListDBManager<T> dataListDBManager, DataListCloudManager<T> dataListCloudManager) {
        this.dataListCacheManager = dataListCacheManager;
        this.dataListDBManager = dataListDBManager;
        this.dataListCloudManager = dataListCloudManager;
    }

    private Observable<Boolean> updateLatestData() {
        return dataListCacheManager.isExpired().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) {
                    return Observable.just(false);
                }
                return dataListDBManager.deleteAll().flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return dataListCloudManager.getData().flatMap(new Func1<List<T>, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(List<T> ts) {
                                return dataListDBManager.insertAll(ts).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(Boolean aBoolean) {
                                        return dataListCacheManager.updateExpiredTime();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
