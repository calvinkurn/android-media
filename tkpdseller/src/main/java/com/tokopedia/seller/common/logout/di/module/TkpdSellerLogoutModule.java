package com.tokopedia.seller.common.logout.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.product.data.repository.CategoryRepositoryImpl;
import com.tokopedia.seller.product.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.data.source.CategoryDataSource;
import com.tokopedia.seller.product.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.data.source.FetchCategoryDataSource;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.categorypicker.ClearCategoryCacheUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@TkpdSellerLogoutScope
@Module
public class TkpdSellerLogoutModule {

    @TkpdSellerLogoutScope
    @Provides
    ClearAllDraftProductUseCase provideClearAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository){
        return new ClearAllDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @TkpdSellerLogoutScope
    @Provides
    ClearCategoryCacheUseCase provideClearCategoryCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository){
        return new ClearCategoryCacheUseCase(threadExecutor, postExecutionThread, categoryRepository);
    }

    @TkpdSellerLogoutScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource dataSource){
        return new ProductDraftRepositoryImpl(dataSource);
    }

    @TkpdSellerLogoutScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource, CategoryDataSource categoryDataSource, FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @TkpdSellerLogoutScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }
}
