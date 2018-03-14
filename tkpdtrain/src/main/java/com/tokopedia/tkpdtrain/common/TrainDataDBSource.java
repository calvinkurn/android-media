package com.tokopedia.tkpdtrain.common;

import com.tokopedia.tkpdtrain.common.specification.Specification;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/7/18.
 */

public interface TrainDataDBSource<T, U> {
    Observable<Boolean> isDataAvailable();

    Observable<Boolean> deleteAll();

    Observable<Boolean> insert(T data);

    Observable<Boolean> insertAll(List<T> datas);

    Observable<List<U>> getDatas(Specification specification);

    Observable<Integer> getCount(Specification specification);

    Observable<U> getData(Specification specification);


}
