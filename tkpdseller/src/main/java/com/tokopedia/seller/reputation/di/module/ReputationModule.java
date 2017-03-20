package com.tokopedia.seller.reputation.di.module;


import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.seller.reputation.data.mapper.ReputationReviewMapper;
import com.tokopedia.seller.reputation.data.repository.ReputationReviewRepositoryImpl;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.di.scope.ReputationScope;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationMergeUseCase;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationUseCase;
import com.tokopedia.seller.reputation.domain.interactor.ShopInfoUseCase;
import com.tokopedia.seller.reputation.network.apiservice.api.SellerReputationApi;
import com.tokopedia.seller.util.ShopNetworkController;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.tokopedia.seller.reputation.constant.ReputationConstant.REPUTATION_TAG;

/**
 * @author normansyahputa on 3/15/17.
 */
@ReputationScope
@Module
public class ReputationModule {

    @ReputationScope
    @Provides
    ReputationReviewRepository provideReputationReviewRepository(
            CloudReputationReviewDataSource cloudReputationReviewDataSource,
            ShopNetworkController shopNetworkController
    ) {
        return new ReputationReviewRepositoryImpl(
                cloudReputationReviewDataSource,
                shopNetworkController);
    }

    @ReputationScope
    @Provides
    ShopInfoUseCase provideShopInfoUseCase(
            JobExecutor jobExecutor,
            PostExecutionThread postExecutionThread,
            ReputationReviewRepository reputationReviewRepository
    ) {
        return new ShopInfoUseCase(
                jobExecutor, postExecutionThread, reputationReviewRepository);
    }

    @ReputationScope
    @Provides
    ReviewReputationMergeUseCase provideReviewReputationMergeUseCase(
            JobExecutor jobExecutor,
            PostExecutionThread postExecutionThread,
            ReviewReputationUseCase reviewReputationUseCase,
            ShopInfoUseCase shopInfoUseCase
    ) {
        return new ReviewReputationMergeUseCase(
                jobExecutor, postExecutionThread, reviewReputationUseCase, shopInfoUseCase
        );
    }


    @ReputationScope
    @Provides
    CloudReputationReviewDataSource provideCloudReputationReviewDataSource(
            Context context,
            SellerReputationApi sellerReputationApi,
            ReputationReviewMapper reputationReviewMapper
    ) {
        return new CloudReputationReviewDataSource(
                context, sellerReputationApi, reputationReviewMapper
        );
    }

    @ReputationScope
    @Provides
    ReputationReviewMapper provideReputationReviewMapper() {
        return new ReputationReviewMapper();
    }

    @ReputationScope
    @Provides
    SellerReputationApi provideTopAdsSearchProductService(
            @Named(REPUTATION_TAG) Retrofit retrofit) {
        return retrofit.create(SellerReputationApi.class);
    }

    @ReputationScope
    @Provides
    ReviewReputationUseCase provideReviewReputationUseCase(
            JobExecutor jobExecutor,
            PostExecutionThread postExecutionThread,
            ReputationReviewRepository reputationReviewRepository
    ) {
        return new ReviewReputationUseCase(
                jobExecutor, postExecutionThread, reputationReviewRepository
        );
    }
}
