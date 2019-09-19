package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.autocomplete.presentation.presenter.AutoCompletePresenter;
import com.tokopedia.discovery.imagesearch.di.module.ImageSearchModule;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenterImpl;
import com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by henrypriyono on 10/10/17.
 */

@DiscoveryScope
@Module(includes = {
        ProductModule.class,
        ImageSearchModule.class,
        BannerModule.class,
        ApiModule.class,
        CatalogModule.class,
        AttributeModule.class
})
public class SearchModule {

    @DiscoveryScope
    @Provides
    ImageProductListPresenter provideImageProductListPresenter(@ApplicationContext Context context) {
        return new ImageProductListPresenterImpl(context);
    }

    @DiscoveryScope
    @Provides
    AutoCompletePresenter provideAutoCompletePresenter(@ApplicationContext Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new AutoCompletePresenter(context, getProductUseCase, getImageSearchUseCase);
    }

    @DiscoveryScope
    @Provides
    DiscoveryTracking provideSearchTracking(@ApplicationContext Context context,
                                            UserSessionInterface userSessionInterface) {
        return new DiscoveryTracking(context, userSessionInterface);
    }

    @DiscoveryScope
    @Provides
    PermissionCheckerHelper providePermissionCheckerHelper() {
        return new PermissionCheckerHelper();
    }

}
