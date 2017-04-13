package com.tokopedia.inbox.rescenter.detailv2.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.GetHistoryAwbUseCase;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingFragmentImpl;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */

@ResolutionDetailScope
@Module
public class HistoryAwbModule {

    private final HistoryShippingFragmentView viewListener;

    public HistoryAwbModule(HistoryShippingFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @ResolutionDetailScope
    @Provides
    HistoryShippingFragmentImpl provideHistoryShippingFragmentImpl(GetHistoryAwbUseCase getHistoryAwbUseCase,
                                                                   TrackAwbReturProductUseCase trackAwbReturProductUseCase) {
        return new HistoryShippingFragmentImpl(
                viewListener,
                getHistoryAwbUseCase,
                trackAwbReturProductUseCase
        );
    }

    @ResolutionDetailScope
    @Provides
    GetHistoryAwbUseCase provideGetHistoryAwbUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     ResCenterRepository resCenterRepository) {
        return new GetHistoryAwbUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDetailScope
    @Provides
    TrackAwbReturProductUseCase provideTrackAwbReturProductUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   ResCenterRepository resCenterRepository) {
        return new TrackAwbReturProductUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }
}
