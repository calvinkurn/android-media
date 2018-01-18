package com.tokopedia.tokocash.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tokocash.apiservice.WalletService;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.GetReasonHelpDataUseCase;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;

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

    @Provides
    @TokoCashScope
    GetInfoQrTokoCashUseCase provideGetInfoQrTokoCashUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             QrPaymentRepository qrPaymentRepository) {
        return new GetInfoQrTokoCashUseCase(threadExecutor, postExecutionThread, qrPaymentRepository);
    }

    @Provides
    @TokoCashScope
    PostQrPaymentUseCase providePostQrPaymentUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     QrPaymentRepository qrPaymentRepository) {
        return new PostQrPaymentUseCase(threadExecutor, postExecutionThread, qrPaymentRepository);
    }

    @Provides
    @TokoCashScope
    GetBalanceTokoCashUseCase provideGetBalanceTokoCashUseCase(ThreadExecutor threadExecutor,
                                                          PostExecutionThread postExecutionThread,
                                                          QrPaymentRepository qrPaymentRepository) {
        return new GetBalanceTokoCashUseCase(threadExecutor, postExecutionThread, qrPaymentRepository);
    }
}
