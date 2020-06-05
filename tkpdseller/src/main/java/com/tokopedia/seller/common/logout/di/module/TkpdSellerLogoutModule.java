package com.tokopedia.seller.common.logout.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.category.di.module.CategoryPickerModule;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.interactor.ClearCategoryCacheUseCase;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDB;
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDao;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@TkpdSellerLogoutScope
@Module(includes = {CategoryPickerModule.class})
public class TkpdSellerLogoutModule {

    @TkpdSellerLogoutScope
    @Provides
    ClearAllDraftProductUseCase provideClearAllDraftProductUseCase(AddEditProductDraftRepository productDraftRepository){
        return new ClearAllDraftProductUseCase(productDraftRepository);
    }

    @TkpdSellerLogoutScope
    @Provides
    ClearCategoryCacheUseCase provideClearCategoryCacheUseCase(CategoryRepository categoryRepository){
        return new ClearCategoryCacheUseCase(categoryRepository);
    }

    @TkpdSellerLogoutScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource dataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(dataSource, context);
    }

    @TkpdSellerLogoutScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context);
    }

    @TkpdSellerLogoutScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB){
        return productDraftDB.getProductDraftDao();
    }

    @TkpdSellerLogoutScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @TkpdSellerLogoutScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

}
