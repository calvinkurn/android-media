package com.tokopedia.tkpdstream.channel.di;

import com.tokopedia.tkpdstream.channel.data.ChannelApi;
import com.tokopedia.tkpdstream.common.data.BaseUrl;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/3/18.
 */

@ChannelScope
@Module
public class ChannelModule {

    @ChannelScope
    @Provides
    @GroupChatQualifier
    public Retrofit provideChannelRetrofit(Retrofit.Builder retrofitBuilder,
                                           OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(BaseUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChannelScope
    @Provides
    public ChannelApi provideChannelApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(ChannelApi.class);
    }

}
