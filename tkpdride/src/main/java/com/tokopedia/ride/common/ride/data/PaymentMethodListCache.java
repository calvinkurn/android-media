package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;

import rx.Observable;

/**
 * Created by Vishal on 3rd Nov, 2017
 */

public interface PaymentMethodListCache {

    void put(PaymentMethodListEntity entities);

    boolean isCached();

    void evictAll();

    Observable<PaymentMethodListEntity> getCache();
}
