package com.tokopedia.inbox.rescenter.detailv2.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentContract;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/13/17.
 */

@ResolutionDetailScope
@Module
public class ResolutionProductDetailModule {

    private final ProductDetailFragmentContract.ViewListener viewListener;

    public ResolutionProductDetailModule(ProductDetailFragmentContract.ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @ResolutionDetailScope
    @Provides
    ProductDetailFragmentImpl provideProductDetailFragmentImpl(
            GetProductDetailUseCase getProductDetailUseCase) {
        return new ProductDetailFragmentImpl(viewListener, getProductDetailUseCase);
    }

    @ResolutionDetailScope
    @Provides
    GetProductDetailUseCase provideGetProductDetailUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           ResCenterRepository resCenterRepository) {
        return new GetProductDetailUseCase(threadExecutor, postExecutionThread, resCenterRepository);
    }
}
