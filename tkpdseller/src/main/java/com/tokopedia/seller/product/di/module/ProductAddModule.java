package com.tokopedia.seller.product.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.data.repository.ImageProductUploadRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ProductScoreRepositoryImpl;
import com.tokopedia.seller.product.data.repository.UploadProductRepositoryImpl;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.ProductScoreDataSource;
import com.tokopedia.seller.product.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.seller.product.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.data.source.cloud.api.UploadProductApi;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.ProductScoreRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module
public class ProductAddModule {

    @ProductAddScope
    @Provides
    ProductAddPresenter provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase, AddProductUseCase addProductUseCase,
                                                   ProductScoringUseCase productScoringUseCase){
        return new ProductAddPresenterImpl(saveDraftProductUseCase, productScoringUseCase, addProductUseCase);
    }

    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource){
        return new ProductDraftRepositoryImpl(productDraftDataSource);
    }

    @ProductAddScope
    @Provides
    UploadProductRepository provideUploadProductRepository(UploadProductDataSource uploadProductDataSource){
        return new UploadProductRepositoryImpl(uploadProductDataSource);
    }

    @ProductAddScope
    @Provides
    ImageProductUploadRepository provideImageProductUploadRepository(ImageProductUploadDataSource iageProductUploadDataSource){
        return new ImageProductUploadRepositoryImpl(iageProductUploadDataSource);
    }

    @ProductAddScope
    @Provides
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @ProductAddScope
    @Provides
    UploadProductApi provideUploadProductApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(UploadProductApi.class);
    }

    @ProductAddScope
    @Provides
    ProductScoringUseCase provideProductScoringUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductScoreRepository productScoreRepository){
        return new ProductScoringUseCase(threadExecutor, postExecutionThread, productScoreRepository);
    }

    @ProductAddScope
    @Provides
    ProductScoreRepository provideProductScoreRepo(ProductScoreDataSource productScoreDataSource){
        return new ProductScoreRepositoryImpl(productScoreDataSource);
    }

    @ProductAddScope
    @Provides
    ProductScoreDataSource provideProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache){
        return new ProductScoreDataSource(productScoreDataSourceCache);
    }

    @ProductAddScope
    @Provides
    ProductScoreDataSourceCache provideProductScoreDataSourceCache(@ActivityContext Context context, Gson gson){
        return new ProductScoreDataSourceCache(context, gson);
    }
}
