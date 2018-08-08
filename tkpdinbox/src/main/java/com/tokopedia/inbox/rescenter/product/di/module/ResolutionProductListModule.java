package com.tokopedia.inbox.rescenter.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.di.scope.ResolutionProductListScope;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetListProductUseCase;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentImpl;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */

@ResolutionProductListScope
@Module
public class ResolutionProductListModule {

    private final ListProductFragmentView viewListener;

    public ResolutionProductListModule(ListProductFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @ResolutionProductListScope
    @Provides
    ListProductFragmentImpl provideListProductFragmentImpl(
            GetListProductUseCase getListProductUseCase) {
        return new ListProductFragmentImpl(viewListener, getListProductUseCase);
    }

    @ResolutionProductListScope
    @Provides
    GetListProductUseCase provideGetListProductUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       ResCenterRepository resCenterRepository) {
        return new GetListProductUseCase(threadExecutor, postExecutionThread, resCenterRepository);
    }
}
