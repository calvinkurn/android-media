package com.tokopedia.discovery.newdiscovery.di.module;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.core.network.apiservices.search.apis.HotListApi;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoWishlistActionQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class ApiModule {

    @Provides
    BrowseApi provideSearchApi(@AceQualifier Retrofit retrofit) {
        return retrofit.create(BrowseApi.class);
    }

    @Provides
    HotListApi provideHotlistApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(HotListApi.class);
    }

    @Provides
    MojitoApi provideMojitoApi(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoApi.class);
    }

    @MojitoGetWishlistQualifier
    @Provides
    MojitoApi mojitoGetWishlist(@MojitoGetWishlistQualifier Retrofit retrofit){
        return retrofit.create(MojitoApi.class);
    }

    @MojitoWishlistActionQualifier
    @Provides
    MojitoAuthApi mojitoWishlistAction(@MojitoWishlistActionQualifier Retrofit retrofit){
        return retrofit.create(MojitoAuthApi.class);
    }

    @Provides
    HadesApi provideHadesApi(@HadesQualifier Retrofit retrofit) {
        return retrofit.create(HadesApi.class);
    }
}
