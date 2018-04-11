package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/26/18.
 */

public class MoveToSaldoUseCase extends UseCase<WithdrawSaldo> {

    public static final String REFUND_ID = "refund_id";
    public static final String REFUND_TYPE = "refund_type";

    private IWalletRepository walletRepository;
    private String url;

    public MoveToSaldoUseCase(IWalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Observable<WithdrawSaldo> createObservable(RequestParams requestParams) {
        return walletRepository.withdrawTokoCashToSaldo(url, requestParams.getParamsAllValueInString());
    }
}
