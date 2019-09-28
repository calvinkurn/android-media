package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.user.session.UserSession;
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
        BannerModule.class,
        ApiModule.class,
        CatalogModule.class,
        AttributeModule.class
})
public class DiscoveryModule {

    @DiscoveryScope
    @Provides
    DiscoveryTracking provideDiscoveryTracking(UserSessionInterface userSessionInterface) {
        return new DiscoveryTracking(userSessionInterface);
    }

    @DiscoveryScope
    @Provides
    PermissionCheckerHelper providePermissionCheckerHelper() {
        return new PermissionCheckerHelper();
    }

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
