package com.tokopedia.seller.gmstat.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.seller.gmstat.apis.GMStatApi;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.GMSTAT_TAG;

/**
 * Created by normansyahputa on 11/2/16.
 */

@Module
public class GMStatModules {
    @Provides
    @Singleton
    public GMStatApi provideGmStatApi(@Named(GMSTAT_TAG)Retrofit retrofit){
        return retrofit.create(GMStatApi.class);
    }

    @Provides
    public GMStatNetworkController provideGmStatNetworkController(Context context, Gson gson, GMStatApi gmStatApi){
        return new GMStatNetworkController(context, gson, gmStatApi);
    }
}
