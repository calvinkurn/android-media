package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public interface BalanceDataSource {

    Observable<BalanceTokoCashEntity> getBalanceTokoCash();

}
