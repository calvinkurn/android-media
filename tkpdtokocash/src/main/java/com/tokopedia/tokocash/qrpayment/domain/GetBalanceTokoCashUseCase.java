package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.tokocash.qrpayment.data.repository.BalanceRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.ActionBalance;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {

    private BalanceRepository repository;

    public GetBalanceTokoCashUseCase(BalanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<BalanceTokoCash> createObservable(RequestParams requestParams) {
        return repository.getLocalBalanceTokoCash()
                .onErrorResumeNext(new Func1<Throwable, Observable<BalanceTokoCash>>() {
                    @Override
                    public Observable<BalanceTokoCash> call(Throwable throwable) {
                        return repository.getBalanceTokoCash();
                    }
                })
                .onErrorReturn(new Func1<Throwable, BalanceTokoCash>() {
                    @Override
                    public BalanceTokoCash call(Throwable throwable) {
                        if (throwable instanceof UserInactivateTokoCashException) {
                            ActionBalance action = new ActionBalance();
                            action.setLabelAction("Aktivasi TokoCash");
                            action.setApplinks("tokopedia://wallet/activation");
                            BalanceTokoCash tokoCashData = new BalanceTokoCash();
                            tokoCashData.setBalance("");
                            tokoCashData.setTitleText("TokoCash");
                            tokoCashData.setActionBalance(action);
                            return tokoCashData;
                        }
                        return null;
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                });
    }
}
