package com.tokopedia.tkpdstream.channel.di;

import com.tokopedia.tkpdstream.channel.domain.mapper.ChannelMapper;
import com.tokopedia.tkpdstream.channel.domain.source.ChannelSource;
import com.tokopedia.tkpdstream.channel.domain.usecase.GetChannelListUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.common.data.GroupChatApi;
import com.tokopedia.tkpdstream.common.data.GroupChatUrl;
import com.tokopedia.tkpdstream.common.di.module.NetModule;
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

    @ChannelScope
    @Provides
    @GroupChatQualifier
    public Retrofit provideGroupChatRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChannelScope
    @Provides
    public GroupChatApi provideGroupChatApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(GroupChatApi.class);
    }

    @ChannelScope
    @Provides
    public GetChannelListUseCase provideGetChannelListUseCase(ChannelSource channelSource) {
        return new GetChannelListUseCase(channelSource);
    }

    @ChannelScope
    @Provides
    public ChannelSource provideChannelSource(GroupChatApi groupChatApi, ChannelMapper channelMapper){
        return new ChannelSource(groupChatApi, channelMapper);
    }
}
