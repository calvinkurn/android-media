package com.tokopedia.tokocash.qrpayment.data.repository;

import com.tokopedia.tokocash.qrpayment.data.datasource.BalanceDataSourceFactory;
import com.tokopedia.tokocash.qrpayment.data.mapper.BalanceTokoCashMapper;
import com.tokopedia.tokocash.qrpayment.domain.IBalanceRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceRepository implements IBalanceRepository {

    private BalanceDataSourceFactory balanceDataSourceFactory;
    private BalanceTokoCashMapper balanceTokoCashMapper;

    @Inject
    public BalanceRepository(BalanceDataSourceFactory balanceDataSourceFactory,
                             BalanceTokoCashMapper balanceTokoCashMapper) {
        this.balanceDataSourceFactory = balanceDataSourceFactory;
        this.balanceTokoCashMapper = balanceTokoCashMapper;
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash() {
        return balanceDataSourceFactory.createBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(balanceTokoCashMapper);
    }

    @Override
    public Observable<BalanceTokoCash> getLocalBalanceTokoCash() {
        return balanceDataSourceFactory.createLocalBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(balanceTokoCashMapper);
    }

}
