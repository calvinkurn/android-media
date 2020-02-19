package com.tokopedia.discovery.newdiscovery.category.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.category.di.scope.CategoryScope;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.module.AttributeModule;
import com.tokopedia.discovery.newdiscovery.di.module.BannerModule;
import com.tokopedia.discovery.newdiscovery.di.module.CatalogModule;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;
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
        CatalogModule.class,
})
public class CategoryModule {

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
