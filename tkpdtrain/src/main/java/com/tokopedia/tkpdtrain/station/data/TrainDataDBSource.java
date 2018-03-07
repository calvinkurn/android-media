package com.tokopedia.tkpdtrain.station.data;

import com.tokopedia.abstraction.base.data.source.database.DataDBSource;
import com.tokopedia.tkpdtrain.station.data.specification.Specification;

import java.util.HashMap;
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
