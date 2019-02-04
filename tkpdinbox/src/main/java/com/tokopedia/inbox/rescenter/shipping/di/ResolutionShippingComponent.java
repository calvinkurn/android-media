package com.tokopedia.inbox.rescenter.shipping.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Qualifier;
import com.tokopedia.inbox.rescenter.di.ResolutionScope;

import dagger.Component;
import retrofit2.Retrofit;

@ResolutionScope
@Component(modules = ResolutionShippingModule.class, dependencies = AppComponent.class)
public interface ResolutionShippingComponent {

    @UploadWsV4Qualifier
    Retrofit uploadWsV4Retrofit();

}
