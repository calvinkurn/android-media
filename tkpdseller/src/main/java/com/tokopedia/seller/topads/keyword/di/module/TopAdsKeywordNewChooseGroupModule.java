package com.tokopedia.seller.topads.keyword.di.module;

import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 5/24/17.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordNewChooseGroupModule extends TopAdsModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordNewChooseGroupPresenter providePresenter(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase){
        return new TopAdsKeywordNewChooseGroupPresenterImpl(topAdsSearchGroupAdsNameUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsGroupAdsRepository provideTopAdsGroupRepository(TopAdsGroupAdFactory topAdsGroupAdFactory){
        return new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsManagementApi provideManagementApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

}
