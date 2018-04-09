package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class PostHelpHistoryDetailUseCase extends UseCase<Boolean> {

    public static final String SUBJECT = "subject";
    public static final String MESSAGE = "message";
    public static final String CATEGORY = "category";
    public static final String TRANSACTION_ID = "transaction_id";

    private WalletRepository walletRepository;

    public PostHelpHistoryDetailUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return walletRepository.submitHelpHistory(requestParams.getParamsAllValueInString());
    }
}
