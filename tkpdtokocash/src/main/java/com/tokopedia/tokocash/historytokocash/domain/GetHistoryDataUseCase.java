package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public class GetHistoryDataUseCase extends UseCase<TokoCashHistoryData> {

    public static final String TYPE = "type";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String PAGE = "page";

    private WalletRepository walletRepository;

    public GetHistoryDataUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Observable<TokoCashHistoryData> createObservable(RequestParams requestParams) {
        return walletRepository.getTokoCashHistoryData(requestParams.getParameters());
    }
}
