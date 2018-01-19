package com.tokopedia.mitratoppers.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.mitratoppers.common.di.module.MitraToppersModule;
import com.tokopedia.mitratoppers.common.di.scope.MitraToppersScope;
import com.tokopedia.mitratoppers.dashboard.TestFragment;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by hendry on 18/01/18.
 */
@MitraToppersScope
@Component(modules = MitraToppersModule.class, dependencies = BaseAppComponent.class)
public interface MitraToppersComponent {
    @ApplicationContext
    Context context();

    void inject(TestFragment testFragment);
}
