package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.data.repository.TokoCashBalanceRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {

    private static final String TAG = GetBalanceTokoCashUseCase.class.getName();
    private TokoCashBalanceRepository repository;

    public GetBalanceTokoCashUseCase(TokoCashBalanceRepository repository) {
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
                });
    }
}
