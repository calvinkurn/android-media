package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/26/18.
 */

public interface ITokoCashBalanceRepository {

    Observable<BalanceTokoCash> getBalanceTokoCash();

    Observable<BalanceTokoCash> getLocalBalanceTokoCash();
}
