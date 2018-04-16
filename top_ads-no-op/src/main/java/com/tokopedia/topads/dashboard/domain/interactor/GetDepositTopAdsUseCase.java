package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;

import rx.Observable;

public class GetDepositTopAdsUseCase extends UseCase<DataDeposit> {

    @Override
    public Observable<DataDeposit> createObservable(RequestParams requestParams) {
        return null;
    }

    public static RequestParams createRequestParams(String shopId){
        return RequestParams.create();
    }
}
