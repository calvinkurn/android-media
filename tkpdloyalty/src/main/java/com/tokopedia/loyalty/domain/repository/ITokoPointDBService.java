package com.tokopedia.loyalty.domain.repository;

import rx.Observable;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public interface ITokoPointDBService {

    Observable<GqlTokoPointDrawerDataResponse> getPointDrawer();

    Observable<GqlTokoPointDrawerDataResponse> storePointDrawer(GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse);
}
