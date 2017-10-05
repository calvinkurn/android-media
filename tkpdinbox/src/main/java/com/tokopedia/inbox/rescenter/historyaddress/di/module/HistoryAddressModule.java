package com.tokopedia.inbox.rescenter.historyaddress.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaddress.di.scope.HistoryAddressScope;
import com.tokopedia.inbox.rescenter.historyaddress.domain.interactor.GetHistoryAddressUseCase;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentImpl;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */

@HistoryAddressScope
@Module
public class HistoryAddressModule {

    private final HistoryAddressFragmentView viewListener;

    public HistoryAddressModule(HistoryAddressFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @HistoryAddressScope
    @Provides
    HistoryAddressFragmentImpl provideHistoryAddressFragmentImpl(
            GetHistoryAddressUseCase getHistoryAddressUseCase) {
        return new HistoryAddressFragmentImpl(viewListener, getHistoryAddressUseCase);
    }

    @HistoryAddressScope
    @Provides
    GetHistoryAddressUseCase provideGetHistoryAddressUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ResCenterRepository resCenterRepository) {
        return new GetHistoryAddressUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }
}
