package com.tokopedia.seller.product.manage.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.manage.data.repository.ActionProductManageRepositoryImpl;
import com.tokopedia.seller.product.manage.data.source.ActionProductManageDataSource;
import com.tokopedia.seller.product.manage.data.source.ProductActionApi;
import com.tokopedia.seller.product.manage.domain.ActionProductManageRepository;
import com.tokopedia.seller.product.manage.domain.DeleteProductUseCase;
import com.tokopedia.seller.product.manage.domain.EditPriceProductUseCase;
import com.tokopedia.seller.product.manage.domain.MultipleDeleteProductUseCase;
import com.tokopedia.seller.product.manage.view.mapper.GetProductListManageMapperView;
import com.tokopedia.seller.product.manage.view.presenter.ProductManagePresenter;
import com.tokopedia.seller.product.manage.view.presenter.ProductManagePresenterImpl;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.repository.GetProductListSellingRepositoryImpl;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@Module
@ProductManageScope
public class ProductManageModule {
    @Provides
    @ProductManageScope
    public ProductManagePresenter provideManageProductPresenter(GetShopInfoUseCase getShopInfoUseCase,
                                                                GetProductListSellingUseCase getProductListSellingUseCase,
                                                                EditPriceProductUseCase editPriceProductUseCase,
                                                                DeleteProductUseCase deleteProductUseCase,
                                                                GetProductListManageMapperView getProductListManageMapperView,
                                                                SellerModuleRouter sellerModuleRouter,
                                                                MultipleDeleteProductUseCase multipleDeleteProductUseCase){
        return new ProductManagePresenterImpl(getShopInfoUseCase, getProductListSellingUseCase, editPriceProductUseCase, deleteProductUseCase, getProductListManageMapperView,sellerModuleRouter, multipleDeleteProductUseCase);
    }

    @Provides
    @ProductManageScope
    public GetProductListSellingRepository provideGetProductListSellingRepository(GetProductListSellingDataSource getProductListSellingDataSource){
        return new GetProductListSellingRepositoryImpl(getProductListSellingDataSource);
    }

    @Provides
    @ProductManageScope
    public GetProductListSellerApi provideGetProductListSellerApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GetProductListSellerApi.class);
    }

    @Provides
    @ProductManageScope
    public ActionProductManageRepository provideActionManageProductRepository(ActionProductManageDataSource actionProductManageDataSource){
        return new ActionProductManageRepositoryImpl(actionProductManageDataSource);
    }

    @Provides
    @ProductManageScope
    public ProductActionApi provideProductActionApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ProductActionApi.class);
    }

    @Provides
    @ProductManageScope
    public TomeProductApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @Provides
    @ProductManageScope
    public SellerModuleRouter provideSellerModuleRouter(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter){
            return ((SellerModuleRouter)context);
        }else{
            return null;
        }
    }
}
