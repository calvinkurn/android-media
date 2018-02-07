package com.tokopedia.discovery.newdiscovery.hotlist.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.module.AttributeModule;
import com.tokopedia.discovery.newdiscovery.di.module.BannerModule;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.di.scope.HotlistScope;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistInitializeUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistLoadMoreUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 10/5/17.
 */
@HotlistScope
@Module(includes = {ProductModule.class,
        ApiModule.class,
        BannerModule.class,
        AttributeModule.class})
public class HotlistModule {

    @HotlistScope
    @Provides
    HotlistPresenter provideHotlistPresenter(GetProductUseCase getProductUseCase) {
        return new HotlistPresenter(getProductUseCase);
    }

    @HotlistScope
    @Provides
    HotlistFragmentPresenter provideHotlistFragmentPresenter(@ApplicationContext Context context) {
        return new HotlistFragmentPresenter(context);
    }

    @HotlistScope
    @Provides
    GetHotlistInitializeUseCase getHotlistInitializeUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GetProductUseCase getProductUseCase,
            BannerRepository bannerRepository,
            AttributeRepository attributeRepository) {
        return new GetHotlistInitializeUseCase(
                threadExecutor,
                postExecutionThread,
                getProductUseCase,
                bannerRepository,
                attributeRepository
        );
    }

    @HotlistScope
    @Provides
    GetHotlistLoadMoreUseCase getHotlistLoadMoreUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ProductRepository productRepository) {
        return new GetHotlistLoadMoreUseCase(
                threadExecutor,
                postExecutionThread,
                productRepository
        );
    }
}
