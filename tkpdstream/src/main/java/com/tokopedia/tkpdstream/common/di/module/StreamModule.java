package com.tokopedia.tkpdstream.common.di.module;


import com.tokopedia.tkpdstream.common.data.GroupChatApi;
import com.tokopedia.tkpdstream.common.data.GroupChatUrl;
import com.tokopedia.tkpdstream.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Module(includes = {NetModule.class})
public class StreamModule {

    @StreamScope
    @Provides
    @GroupChatQualifier
    public Retrofit provideStreamRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build();
    }

    @StreamScope
    @Provides
    public GroupChatApi provideGroupChatApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(GroupChatApi.class);
    }

}
