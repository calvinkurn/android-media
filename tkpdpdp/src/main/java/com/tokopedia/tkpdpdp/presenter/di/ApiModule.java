package com.tokopedia.tkpdpdp.presenter.di;

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
import com.tokopedia.core.network.di.qualifier.MojitoWishlistCountQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class ApiModule {
    @MojitoGetWishlistQualifier
    @Provides
    MojitoApi mojitoGetWishlistCount(@MojitoGetWishlistQualifier Retrofit retrofit){
        return retrofit.create(MojitoApi.class);
    }
}
