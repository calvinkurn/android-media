package com.tokopedia.tkpdstream.chatroom.di;

import com.tokopedia.tkpdstream.channel.di.ChannelScope;
import com.tokopedia.tkpdstream.common.data.GroupChatApi;
import com.tokopedia.tkpdstream.common.data.GroupChatUrl;
import com.tokopedia.tkpdstream.common.data.VoteApi;
import com.tokopedia.tkpdstream.common.data.VoteUrl;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.tkpdstream.common.di.qualifier.VoteQualifier;
import com.tokopedia.tkpdstream.vote.domain.mapper.GetVoteMapper;
import com.tokopedia.tkpdstream.vote.domain.source.GetVoteSource;
import com.tokopedia.tkpdstream.vote.domain.usecase.GetVoteUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Module
public class ChatroomModule {

    @ChatroomScope
    @Provides
    @VoteQualifier
    public Retrofit provideGroupChatRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(VoteUrl.BASE_URL).client(okHttpClient).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    @ChatroomScope
    @Provides
    public VoteApi provideVoteApi(@VoteQualifier Retrofit retrofit) {
        return retrofit.create(VoteApi.class);
    }

    @ChatroomScope
    @Provides
    public GetVoteSource provideGetVoteSource(VoteApi voteApi, GetVoteMapper getVoteMapper){
        return new GetVoteSource(voteApi, getVoteMapper);
    }


    @ChatroomScope
    @Provides
    public GetVoteUseCase provideGetVoteUseCase(GetVoteSource getVoteSource) {
        return new GetVoteUseCase(getVoteSource);
    }

}
