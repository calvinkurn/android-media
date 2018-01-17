package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

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

    public GetHistoryDataUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 WalletRepository walletRepository) {
        super(threadExecutor, postExecutionThread);
        this.walletRepository = walletRepository;
    }

    @Override
    public Observable<TokoCashHistoryData> createObservable(RequestParams requestParams) {
        return walletRepository.getTokoCashHistoryData(requestParams.getParameters());
    }
}
