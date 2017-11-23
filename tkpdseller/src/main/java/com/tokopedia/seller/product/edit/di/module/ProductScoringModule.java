package com.tokopedia.seller.product.edit.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.data.repository.ProductScoreRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ProductScoreDataSource;
import com.tokopedia.seller.product.edit.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.DataScoringProduct;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.DataScoringProductBuilder;
import com.tokopedia.seller.product.edit.domain.ProductScoreRepository;
import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.edit.view.presenter.ProductScoringDetailPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductScoringDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

@ActivityScope
@Module
public class ProductScoringModule {

    @ActivityScope
    @Provides
    ProductScoringDetailPresenter provideProductScoringPresenter(ProductScoringUseCase productScoringUseCase){
        return new ProductScoringDetailPresenterImpl(productScoringUseCase);
    }

    @ActivityScope
    @Provides
    ProductScoringUseCase provideProductScoringUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductScoreRepository productScoreRepository){
        return new ProductScoringUseCase(threadExecutor, postExecutionThread, productScoreRepository);
    }

    @ActivityScope
    @Provides
    ProductScoreRepository provideProductScoreRepo(ProductScoreDataSource productScoreDataSource){
        return new ProductScoreRepositoryImpl(productScoreDataSource);
    }

    @ActivityScope
    @Provides
    ProductScoreDataSource provideProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache){
        return new ProductScoreDataSource(productScoreDataSourceCache);
    }

    @ActivityScope
    @Provides
    ProductScoreDataSourceCache provideProductScoreDataSourceCache(DataScoringProduct dataScoringProduct){
        return new ProductScoreDataSourceCache(dataScoringProduct);
    }

    @ActivityScope
    @Provides
    DataScoringProduct provideDataScoringProduct(@ApplicationContext Context context, Gson gson){
        return new DataScoringProductBuilder(context, gson).build();
    }

}
