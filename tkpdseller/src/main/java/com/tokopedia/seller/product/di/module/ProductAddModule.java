package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.product.data.repository.CatalogRepositoryImpl;
import com.tokopedia.seller.product.data.repository.CategoryRecommRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ImageProductUploadRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.data.repository.UploadProductRepositoryImpl;
import com.tokopedia.seller.product.data.source.CatalogDataSource;
import com.tokopedia.seller.product.data.source.CategoryRecommDataSource;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.MerlinApi;
import com.tokopedia.seller.product.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.data.source.cloud.api.SearchApi;
import com.tokopedia.seller.product.data.source.cloud.api.UploadProductApi;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.CatalogRepository;
import com.tokopedia.seller.product.domain.CategoryRecommRepository;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
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
    ProductAddPresenter provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                                   AddProductUseCase addProductUseCase,
                                                   FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                                   GetCategoryRecommUseCase getCategoryRecommUseCase){
        return new ProductAddPresenterImpl(saveDraftProductUseCase, addProductUseCase,
                fetchCatalogDataUseCase, getCategoryRecommUseCase);
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
    GenerateHostApi provideGenerateHostApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @ProductAddScope
    @Provides
    UploadProductApi provideUploadProductApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(UploadProductApi.class);
    }

    // FOR SEARCH CATALOG
    @ProductAddScope
    @Provides
    CatalogRepository provideCatalogRepository(CatalogDataSource catalogDataSource){
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @ProductAddScope
    @Provides
    SearchApi provideSearchApi(@AceQualifier Retrofit retrofit){
        return retrofit.create(SearchApi.class);
    }

    // FOR CATEGORY RECOMMENDATION
    @ProductAddScope
    @Provides
    CategoryRecommRepository provideCategoryRecommRepository(CategoryRecommDataSource categoryRecommDataSource){
        return new CategoryRecommRepositoryImpl(categoryRecommDataSource);
    }

    @ProductAddScope
    @Provides
    MerlinApi provideMerlinApi(@MerlinQualifier Retrofit retrofit){
        return retrofit.create(MerlinApi.class);
    }

}
