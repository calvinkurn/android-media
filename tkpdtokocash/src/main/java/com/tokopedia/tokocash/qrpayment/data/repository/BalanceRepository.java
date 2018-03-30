package com.tokopedia.tokocash.qrpayment.data.repository;

import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tokocash.qrpayment.data.datasource.BalanceDataSourceFactory;
import com.tokopedia.tokocash.qrpayment.data.mapper.BalanceTokoCashMapper;
import com.tokopedia.tokocash.qrpayment.domain.IBalanceRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;

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
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());

        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(GetUserAttributesUseCase.PARAM_USER_ID,
                Integer.parseInt(sessionHandler.getLoginID()));

        return balanceDataSourceFactory.createBalanceTokoCashDataSource().getBalanceTokoCash(requestParams)
                .map(balanceTokoCashMapper);
    }

    @Override
    public Observable<BalanceTokoCash> getLocalBalanceTokoCash() {
        return balanceDataSourceFactory.createLocalBalanceTokoCashDataSource().getBalanceTokoCash(RequestParams.EMPTY)
                .map(balanceTokoCashMapper);
    }

}
