package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public class GetReasonHelpDataUseCase extends UseCase<List<HelpHistoryTokoCash>> {

    private WalletRepository walletRepository;

    public GetReasonHelpDataUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    WalletRepository walletRepository) {
        super(threadExecutor, postExecutionThread);
        this.walletRepository = walletRepository;
    }

    @Override
    public Observable<List<HelpHistoryTokoCash>> createObservable(RequestParams requestParams) {
        return walletRepository.getHelpHistoryData();
    }
}
