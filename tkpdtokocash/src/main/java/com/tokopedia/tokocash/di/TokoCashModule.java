package com.tokopedia.tokocash.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tokocash.historytokocash.data.datasource.WalletDataSourceFactory;
import com.tokopedia.tokocash.historytokocash.data.datasource.WalletService;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.GetReasonHelpDataUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@Module
public class TokoCashModule {

    public TokoCashModule() {

    }

    @Provides
    @TokoCashScope
    WalletService provideWalletService() {
        return new WalletService(SessionHandler.getAccessTokenTokoCash());
    }

    @Provides
    @TokoCashScope
    WalletDataSourceFactory provideWalletDataSourceFactory(WalletService walletService) {
        return new WalletDataSourceFactory(walletService);
    }

    @Provides
    @TokoCashScope
    GetHistoryDataUseCase provideGetHistoryDataUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       WalletRepository walletRepository) {
        return new GetHistoryDataUseCase(threadExecutor, postExecutionThread, walletRepository);
    }

    @Provides
    @TokoCashScope
    GetReasonHelpDataUseCase provideGetReasonHelpDataUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             WalletRepository walletRepository) {
        return new GetReasonHelpDataUseCase(threadExecutor, postExecutionThread, walletRepository);
    }
}
