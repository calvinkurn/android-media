package com.tokopedia.seller.manageitem.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.db.CategoryDB;
import com.tokopedia.core.common.category.data.source.db.CategoryDao;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.seller.manageitem.common.listener.ProductAddView;
import com.tokopedia.seller.manageitem.common.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.manageitem.view.presenter.ProductAddPresenterImpl;
import com.tokopedia.seller.manageitem.data.cloud.api.MerlinApi;
import com.tokopedia.seller.manageitem.data.cloud.api.SearchApi;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDB;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDao;
import com.tokopedia.seller.manageitem.data.source.CatalogDataSource;
import com.tokopedia.seller.manageitem.data.source.CategoryRecommDataSource;
import com.tokopedia.seller.manageitem.data.source.ProductDraftDataSource;
import com.tokopedia.seller.manageitem.data.source.ProductVariantDataSource;
import com.tokopedia.seller.manageitem.di.scope.ProductAddScope;
import com.tokopedia.seller.manageitem.domain.repository.CatalogRepository;
import com.tokopedia.seller.manageitem.domain.repository.CatalogRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.CategoryRecommRepository;
import com.tokopedia.seller.manageitem.domain.repository.CategoryRecommRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.ProductVariantRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductVariantRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.usecase.FetchProductVariantByCatUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.SaveDraftProductUseCase;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module(includes = ShopCommonModule.class)
public class ProductAddModule {

    @ProductAddScope
    @Provides
    ProductAddPresenterImpl<ProductAddView> provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                                                       GQLGetShopInfoUseCase gqlGetShopInfoUseCase,
                                                                       UserSessionInterface userSession,
                                                                       FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase){
        return new ProductAddPresenterImpl<>(saveDraftProductUseCase, gqlGetShopInfoUseCase, userSession, fetchProductVariantByCatUseCase);
    }

    @ProductAddScope
    @Provides
    AbstractionRouter abstractionRouter(@ApplicationContext Context context){
        if(context instanceof AbstractionRouter){
            return (AbstractionRouter) context;
        }
        return null;
    }

    @ProductAddScope
    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
    @Provides
    Context context(@ApplicationContext Context context){
        return context;
    }

    @ProductAddScope
    @Provides
    CategoryDao provideCategoryDao(@ApplicationContext Context context){
        return CategoryDB.getInstance(context).getCategoryDao();
    }

    @ProductAddScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryDataSource, fetchCategoryDataSource);
    }

    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductAddScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context);
    }

    @ProductAddScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB){
        return productDraftDB.getProductDraftDao();
    }


    // FOR SEARCH CATALOG
    @ProductAddScope
    @Provides
    CatalogRepository provideCatalogRepository(CatalogDataSource catalogDataSource) {
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @ProductAddScope
    @Provides
    SearchApi provideSearchApi(@AceQualifier Retrofit retrofit) {
        return retrofit.create(SearchApi.class);
    }

    // FOR CATEGORY RECOMMENDATION
    @ProductAddScope
    @Provides
    CategoryRecommRepository provideCategoryRecommRepository(CategoryRecommDataSource categoryRecommDataSource) {
        return new CategoryRecommRepositoryImpl(categoryRecommDataSource);
    }

    @ProductAddScope
    @Provides
    MerlinApi provideMerlinApi(@MerlinQualifier Retrofit retrofit) {
        return retrofit.create(MerlinApi.class);
    }

    // FOR SHOP_INFO
    @ProductAddScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper(){
        return new SimpleDataResponseMapper<>();
    }

    @ProductAddScope
    @Provides
    ProductVariantRepository productVariantRepository(ProductVariantDataSource productVariantDataSource){
        return new ProductVariantRepositoryImpl(productVariantDataSource);
    }

    @ProductAddScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @ProductAddScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
