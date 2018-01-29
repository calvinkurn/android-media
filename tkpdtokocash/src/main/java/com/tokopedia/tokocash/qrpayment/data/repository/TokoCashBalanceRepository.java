package com.tokopedia.tokocash.qrpayment.data.repository;

import com.tokopedia.tokocash.qrpayment.data.datasource.TokoCashBalanceDataSourceFactory;
import com.tokopedia.tokocash.qrpayment.data.mapper.BalanceTokoCashMapper;
import com.tokopedia.tokocash.qrpayment.domain.ITokoCashBalanceRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/26/18.
 */

public class TokoCashBalanceRepository implements ITokoCashBalanceRepository {

    private TokoCashBalanceDataSourceFactory tokoCashBalanceDataSourceFactory;

    @Inject
    public TokoCashBalanceRepository(TokoCashBalanceDataSourceFactory tokoCashBalanceDataSourceFactory) {
        this.tokoCashBalanceDataSourceFactory = tokoCashBalanceDataSourceFactory;
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash() {
        return tokoCashBalanceDataSourceFactory.createBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(new BalanceTokoCashMapper());
    }

    @Override
    public Observable<BalanceTokoCash> getLocalBalanceTokoCash() {
        return tokoCashBalanceDataSourceFactory.createLocalBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(new BalanceTokoCashMapper());
    }
}
