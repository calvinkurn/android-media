package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public class GetDepositTopadsDataSource {

    private final GetDepositTopAdsDataSourceCloud getDepositTopAdsDataSourceCloud;

    @Inject
    public GetDepositTopadsDataSource(GetDepositTopAdsDataSourceCloud getDepositTopAdsDataSourceCloud) {
        this.getDepositTopAdsDataSourceCloud = getDepositTopAdsDataSourceCloud;
    }

    public Observable<DataDeposit> getDeposit(TKPDMapParam<String, String> params) {
        return getDepositTopAdsDataSourceCloud.getDeposit(params);
    }
}
