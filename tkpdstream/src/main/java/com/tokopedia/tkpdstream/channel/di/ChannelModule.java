package com.tokopedia.tkpdstream.channel.di;

import com.tokopedia.tkpdstream.common.data.GroupChatApi;
import com.tokopedia.tkpdstream.common.data.GroupChatUrl;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

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

//    @ChannelScope
//    @Provides
//    @GroupChatQualifier
//    public Retrofit provideGroupChatRetrofit(OkHttpClient okHttpClient,
//                                          Retrofit.Builder retrofitBuilder) {
//        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build();
//    }
//
//    @ChannelScope
//    @Provides
//    public GroupChatApi provideGroupChatApi(@GroupChatQualifier Retrofit retrofit) {
//        return retrofit.create(GroupChatApi.class);
//    }
}
