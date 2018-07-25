package com.tokopedia.seller.reputation.di;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.reputation.data.repository.ReputationRepositoryImpl;
import com.tokopedia.seller.reputation.data.repository.ReputationReviewRepositoryImpl;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.data.source.cloud.apiservice.api.SellerReputationApi;
import com.tokopedia.seller.reputation.domain.ReputationRepository;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.product.edit.common.data.source.cloud.ShopApi;
import com.tokopedia.product.edit.common.domain.repository.ShopInfoRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 2/13/18.
 */

@Module
@SellerReputationScope
public class SellerReputationModule {

    @InboxQualifier
    @SellerReputationScope
    @Provides
    public Retrofit provideInboxRetrofit(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient,
                                         Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.INBOX_DOMAIN).client(okHttpClient).build();
    }

    @Provides
    @SellerReputationScope
    public SellerReputationApi provideProductActionApi(@InboxQualifier Retrofit retrofit){
        return retrofit.create(SellerReputationApi.class);
    }

    @Provides
    @SellerReputationScope
    public ReputationRepository provideReputationRepository(CloudReputationReviewDataSource cloudReputationReviewDataSource){
        return new ReputationRepositoryImpl(cloudReputationReviewDataSource);
    }

    @Provides
    @SellerReputationScope
    public ReputationReviewRepository proReputationReviewRepository(CloudReputationReviewDataSource cloudReputationReviewDataSource, ShopInfoRepositoryImpl shopInfoRepository){
        return new ReputationReviewRepositoryImpl(cloudReputationReviewDataSource, shopInfoRepository);
    }

    @Provides
    @SellerReputationScope
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

}
