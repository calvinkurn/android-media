package com.tokopedia.tkpdstream.chatroom.di;

import com.tokopedia.tkpdstream.chatroom.data.ChatroomApi;
import com.tokopedia.tkpdstream.common.data.BaseUrl;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;

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
        return retrofitBuilder.baseUrl(BaseUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    public ChatroomApi provideChatroomApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(ChatroomApi.class);
    }
}
