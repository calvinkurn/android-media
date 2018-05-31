package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointDrawerDataResponse;

import rx.Observable;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public interface ITokoPointDBService {

    Observable<GqlTokoPointDrawerDataResponse> getPointDrawer();

    Observable<GqlTokoPointDrawerDataResponse> storePointDrawer(GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse);
}
