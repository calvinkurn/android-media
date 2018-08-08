package com.tokopedia.seller.product.picker.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.repository.GetProductListSellingRepositoryImpl;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.product.picker.view.mapper.GetProductListPickerMapperView;
import com.tokopedia.seller.product.picker.view.presenter.ProductListPickerSearchPresenter;
import com.tokopedia.seller.product.picker.view.presenter.ProductListPickerSearchPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/8/17.
 */

@Module
@ProductListScope
public class ProductListModule {

    @ProductListScope
    @Provides
    ProductListPickerSearchPresenter provideListPickerSearchPresenter(GetProductListSellingUseCase getProductListSellingUseCase,
                                                                      GetProductListPickerMapperView getProductListPickerMapperView){
        return new ProductListPickerSearchPresenterImpl(getProductListSellingUseCase, getProductListPickerMapperView);
    }


    @ProductListScope
    @Provides
    GetProductListSellingRepository productListSellingRepository(GetProductListSellingDataSource getProductListSellingDataSource){
        return new GetProductListSellingRepositoryImpl(getProductListSellingDataSource);
    }

    @ProductListScope
    @Provides
    GetProductListSellerApi provideGetProductListApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GetProductListSellerApi.class);
    }

    @ProductListScope
    @Provides
    SellerModuleRouter provideSellerModuleRouter(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter){
            return ((SellerModuleRouter)context);
        }else{
            return null;
        }
    }
}
