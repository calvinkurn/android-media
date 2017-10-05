package com.tokopedia.inbox.rescenter.historyaction.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaction.di.scope.HistoryActionScope;
import com.tokopedia.inbox.rescenter.historyaction.domain.interactor.GetHistoryActionUseCase;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentImpl;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */
@HistoryActionScope
@Module
public class HistoryActionModule {

    private final HistoryActionFragmentView viewListener;

    public HistoryActionModule(HistoryActionFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @HistoryActionScope
    @Provides
    HistoryActionFragmentImpl provideHistoryActionFragmentPresenter(
            GetHistoryActionUseCase getHistoryActionUseCase) {
        return new HistoryActionFragmentImpl(viewListener, getHistoryActionUseCase);
    }

    @HistoryActionScope
    @Provides
    GetHistoryActionUseCase provideGetHistoryActionUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           ResCenterRepository resCenterRepository) {
        return new GetHistoryActionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }
}
