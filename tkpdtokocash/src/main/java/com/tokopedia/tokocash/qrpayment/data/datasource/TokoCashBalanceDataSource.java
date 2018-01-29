package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface TokoCashBalanceDataSource {

    Observable<BalanceTokoCashEntity> getBalanceTokoCash();
}
