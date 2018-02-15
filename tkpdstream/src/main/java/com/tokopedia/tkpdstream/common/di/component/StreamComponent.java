package com.tokopedia.tkpdstream.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpdstream.channel.view.fragment.ChannelFragment;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;
import com.tokopedia.tkpdstream.common.di.module.StreamModule;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Component(modules = StreamModule.class, dependencies = BaseAppComponent.class)
public interface StreamComponent {
    @ApplicationContext
    Context context();

//    OkHttpClient provideOkHttpClient();
//
//    Retrofit.Builder retrofitBuilder();

    void inject(BaseStreamActivity baseChatActivity);

}
