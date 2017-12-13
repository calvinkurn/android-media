package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;

import rx.Observable;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public interface ITokoPointDBService {

    Observable<TokoPointDrawerDataResponse> getPointDrawer();

    Observable<TokoPointDrawerDataResponse> storePointDrawer(
            TokoPointDrawerDataResponse tokoPointDrawerDataResponse);
}
