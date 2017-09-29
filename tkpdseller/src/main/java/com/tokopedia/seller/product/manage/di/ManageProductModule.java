package com.tokopedia.seller.product.manage.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.seller.product.manage.data.repository.ActionManageProductRepositoryImpl;
import com.tokopedia.seller.product.manage.data.source.ActionManageProductDataSource;
import com.tokopedia.seller.product.manage.data.source.ProductActionApi;
import com.tokopedia.seller.product.manage.domain.ActionManageProductRepository;
import com.tokopedia.seller.product.manage.domain.DeleteProductUseCase;
import com.tokopedia.seller.product.manage.domain.EditPriceProductUseCase;
import com.tokopedia.seller.product.manage.view.mapper.GetProductListManageMapperView;
import com.tokopedia.seller.product.manage.view.presenter.ManageProductPresenter;
import com.tokopedia.seller.product.manage.view.presenter.ManageProductPresenterImpl;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.repository.GetProductListSellingRepositoryImpl;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@Module
@ManageProductScope
public class ManageProductModule {
    @Provides
    @ManageProductScope
    public ManageProductPresenter provideManageProductPresenter(GetProductListSellingUseCase getProductListSellingUseCase,
                                                                EditPriceProductUseCase editPriceProductUseCase,
                                                                DeleteProductUseCase deleteProductUseCase,
                                                                GetProductListManageMapperView getProductListManageMapperView,
                                                                SellerModuleRouter sellerModuleRouter){
        return new ManageProductPresenterImpl(getProductListSellingUseCase, editPriceProductUseCase, deleteProductUseCase, getProductListManageMapperView,sellerModuleRouter);
    }

    @Provides
    @ManageProductScope
    public GetProductListSellingRepository provideGetProductListSellingRepository(GetProductListSellingDataSource getProductListSellingDataSource){
        return new GetProductListSellingRepositoryImpl(getProductListSellingDataSource);
    }

    @Provides
    @ManageProductScope
    public GetProductListSellerApi provideGetProductListSellerApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GetProductListSellerApi.class);
    }

    @Provides
    @ManageProductScope
    public ActionManageProductRepository provideActionManageProductRepository(ActionManageProductDataSource actionManageProductDataSource){
        return new ActionManageProductRepositoryImpl(actionManageProductDataSource);
    }

    @Provides
    @ManageProductScope
    public ProductActionApi provideProductActionApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ProductActionApi.class);
    }

    @Provides
    @ManageProductScope
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @Provides
    @ManageProductScope
    public TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeApi.class);
    }

    @Provides
    @ManageProductScope
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @Provides
    @ManageProductScope
    public SellerModuleRouter provideSellerModuleRouter(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter){
            return ((SellerModuleRouter)context);
        }else{
            return null;
        }
    }
}
