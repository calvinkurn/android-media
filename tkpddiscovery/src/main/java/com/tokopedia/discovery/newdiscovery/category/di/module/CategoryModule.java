package com.tokopedia.discovery.newdiscovery.category.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.imagesearch.di.module.ImageSearchModule;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.category.di.scope.CategoryScope;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryPresenter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.ProductPresenter;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.module.AttributeModule;
import com.tokopedia.discovery.newdiscovery.di.module.BannerModule;
import com.tokopedia.discovery.newdiscovery.di.module.CatalogModule;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alifa on 10/26/17.
 */
@CategoryScope
@Module(includes = {ProductModule.class,
        ApiModule.class,
        BannerModule.class,
        AttributeModule.class,
        ImageSearchModule.class,
        CatalogModule.class,
        CategoryHeaderModule.class
})
public class CategoryModule {

    @CategoryScope
    @Provides
    ProductPresenter provideProductPresenter(@ApplicationContext Context context) {
        return new ProductPresenter(context);
    }

    @CategoryScope
    @Provides
    CategoryPresenter provideCategoryPresenter(@ApplicationContext Context context,
                                               GetProductUseCase getProductUseCase,
                                               GetImageSearchUseCase getImageSearchUseCase) {
        return new CategoryPresenter(context, getProductUseCase, getImageSearchUseCase);
    }
}
