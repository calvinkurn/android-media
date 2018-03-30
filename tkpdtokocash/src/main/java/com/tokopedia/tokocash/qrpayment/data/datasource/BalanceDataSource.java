package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.anals.GetTokocashQuery;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface BalanceDataSource {

    Observable<GetTokocashQuery.Data> getBalanceTokoCash(RequestParams requestParams);

}
