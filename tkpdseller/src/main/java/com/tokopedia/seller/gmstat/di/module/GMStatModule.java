package com.tokopedia.seller.gmstat.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.seller.gmstat.apis.GMStatApi;
import com.tokopedia.seller.gmstat.di.scope.GMStatScope;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 6/15/17.
 */
@GMStatScope
@Module
public class GMStatModule {
    @Provides
    @GMStatScope
    public GMStatApi provideGmStatApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }

    @GMStatScope
    @Provides
    public GMStatNetworkController provideGmStatNetworkController(@ApplicationContext Context context, Gson gson, GMStatApi gmStatApi) {
        return new GMStatNetworkController(context, gson, gmStatApi);
    }
}
