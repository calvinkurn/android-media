package com.tokopedia.seller.topads.keyword.di.module;

import com.tokopedia.seller.topads.keyword.data.mapper.TopAdsKeywordDeleteMapper;
import com.tokopedia.seller.topads.keyword.data.repository.TopAdsKeywordDeleteRepositoryImpl;
import com.tokopedia.seller.topads.keyword.data.repository.TopAdsKeywordGetDetailRepositoryImpl;
import com.tokopedia.seller.topads.keyword.data.source.TopAdsKeywordDeleteDataSource;
import com.tokopedia.seller.topads.keyword.data.source.TopAdsKeywordGetDetailDataSource;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordDeleteRepository;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordGetDetailRepository;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordDeleteUseCase;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordGetDetailUseCase;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordDetailPresenterImpl;
import com.tokopedia.seller.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordDetailModule extends TopAdsModule {

    @TopAdsKeywordScope
    @Provides
    TopadsKeywordDetailPresenter providePresenterKeywordDetail(TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase,
                                                               TopAdsKeywordDeleteUseCase topAdsKeywordDeleteUseCase){
        return new TopAdsKeywordDetailPresenterImpl(topAdsKeywordGetDetailUseCase, topAdsKeywordDeleteUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordGetDetailRepository provideTopAdsDetailRepository(TopAdsKeywordGetDetailDataSource topAdsKeywordGetDetailDataSource){
        return new TopAdsKeywordGetDetailRepositoryImpl(topAdsKeywordGetDetailDataSource);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordDeleteRepository provideTopAdsKeywordDetailRepository(TopAdsKeywordDeleteDataSource topAdsKeywordDeleteDataSource,
                                                                       TopAdsKeywordDeleteMapper topAdsKeywordDeleteMapper){
        return new TopAdsKeywordDeleteRepositoryImpl(topAdsKeywordDeleteDataSource,topAdsKeywordDeleteMapper);
    }
}
