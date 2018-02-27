package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public class GetReasonHelpDataUseCase extends UseCase<List<HelpHistoryTokoCash>> {

    private WalletRepository walletRepository;

    public GetReasonHelpDataUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Observable<List<HelpHistoryTokoCash>> createObservable(RequestParams requestParams) {
        return walletRepository.getHelpHistoryData();
    }
}
