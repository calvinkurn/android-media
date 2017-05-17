package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.data.mapper.EditProductFormMapper;
import com.tokopedia.seller.product.data.repository.EditProductFormRepositoryImpl;
import com.tokopedia.seller.product.data.source.EditProductFormDataSource;
import com.tokopedia.seller.product.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.EditProductFormApi;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.EditProductFormRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/21/17.
 */

@ProductAddScope
@Module
public class ProductEditModule extends ProductDraftModule {

    @ProductAddScope
    @Provides
    EditProductFormRepository provideEditProductFormRepository(
            EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper,
            FetchVideoEditProductDataSource fetchVideoEditProductDataSource
    ) {
        return new EditProductFormRepositoryImpl(editProductFormDataSource, editProductFormMapper, fetchVideoEditProductDataSource);
    }

    @ProductAddScope
    @Provides
    EditProductFormApi provideEditProductFormApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(EditProductFormApi.class);
    }

    @ProductAddScope
    @Provides
    GoldMerchantService provideGoldMerchantService() {
        return new GoldMerchantService();
    }

    @ProductAddScope
    @Provides
    FetchVideoEditProductDataSource provideFetchVideoEditProductDataSource(GoldMerchantService goldMerchantService) {
        return new FetchVideoEditProductDataSource(goldMerchantService);
    }
}
