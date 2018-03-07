package com.tokopedia.tkpdstream.chatroom.di;

import com.tokopedia.tkpdstream.chatroom.data.ChatroomApi;
import com.tokopedia.tkpdstream.common.data.VoteApi;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.tkpdstream.common.di.qualifier.VoteQualifier;

import com.tokopedia.tkpdstream.common.data.StreamUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Module
public class ChatroomModule {

    @ChatroomScope
    @Provides
    @GroupChatQualifier
    public Retrofit provideChatroomRetrofit(Retrofit.Builder retrofitBuilder,
                                            OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(StreamUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    public ChatroomApi provideChatroomApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(ChatroomApi.class);
    }

    @ChatroomScope
    @Provides
    @VoteQualifier
    public Retrofit provideVoteRetrofit(Retrofit.Builder retrofitBuilder,
                                        OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(StreamUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    public VoteApi provideVoteApi(@VoteQualifier Retrofit retrofit) {
        return retrofit.create(VoteApi.class);
    }
}
