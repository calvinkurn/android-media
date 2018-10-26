package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;

import rx.Observable;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public interface ITokoPointDBService {

    Observable<HachikoDrawerDataResponse> getPointDrawer();

    Observable<HachikoDrawerDataResponse> storePointDrawer(HachikoDrawerDataResponse gqlTokoPointDrawerDataResponse);
}
