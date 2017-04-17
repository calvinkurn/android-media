package com.tokopedia.inbox.rescenter.detailv2.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetListProductUseCase;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentImpl;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */

@ResolutionDetailScope
@Module
public class ResolutionProductListModule {

    private final ListProductFragmentView viewListener;

    public ResolutionProductListModule(ListProductFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @ResolutionDetailScope
    @Provides
    ListProductFragmentImpl provideListProductFragmentImpl(
            GetListProductUseCase getListProductUseCase) {
        return new ListProductFragmentImpl(viewListener, getListProductUseCase);
    }

    @ResolutionDetailScope
    @Provides
    GetListProductUseCase provideGetListProductUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       ResCenterRepository resCenterRepository) {
        return new GetListProductUseCase(threadExecutor, postExecutionThread, resCenterRepository);
    }
}
