package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.imagesearch.di.module.ImageSearchModule;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenterImpl;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.search.SearchPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListPresenterImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by henrypriyono on 10/10/17.
 */

@SearchScope
@Module(includes = {
        GuidedSearchModule.class,
        ProductModule.class,
        ImageSearchModule.class,
        BannerModule.class,
        ApiModule.class,
        CatalogModule.class,
        ShopModule.class,
        AttributeModule.class
})
public class SearchModule {

    @SearchScope
    @Provides
    ProductListPresenter provideProductListPresenter(@ApplicationContext Context context) {
        return new ProductListPresenterImpl(context);
    }

    @SearchScope
    @Provides
    ImageProductListPresenter provideImageProductListPresenter(@ApplicationContext Context context) {
        return new ImageProductListPresenterImpl(context);
    }

    @SearchScope
    @Provides
    ShopListPresenter provideShopListPresenter(@ApplicationContext Context context) {
        return new ShopListPresenterImpl(context);
    }

    @SearchScope
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new SearchPresenter(context, getProductUseCase, getImageSearchUseCase);
    }

    @SearchScope
    @Provides
    SearchTracking provideSearchTracking(@ApplicationContext Context context,
                                         UserSessionInterface userSessionInterface) {
        return new SearchTracking(context, userSessionInterface);
    }
}
