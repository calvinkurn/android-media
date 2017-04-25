package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.data.repository.GenerateHostRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ImageProductUploadRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.data.repository.UploadProductRepositoryImpl;
import com.tokopedia.seller.product.data.source.GenerateHostDataSource;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.data.source.cloud.api.UploadProductApi;
import com.tokopedia.seller.product.di.scope.AddProductServiceScope;
import com.tokopedia.seller.product.domain.GenerateHostRepository;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.interactor.UploadProductUseCase;
import com.tokopedia.seller.product.view.presenter.AddProductServicePresenter;
import com.tokopedia.seller.product.view.presenter.AddProductServicePresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/20/17.
 */
@AddProductServiceScope
@Module
public class AddProductserviceModule {

    @AddProductServiceScope
    @Provides
    AddProductServicePresenter provideAddProductServicePresenter(UploadProductUseCase uploadProductUseCase){
        return new AddProductServicePresenterImpl(uploadProductUseCase);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource){
        return new ProductDraftRepositoryImpl(productDraftDataSource);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource){
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @AddProductServiceScope
    @Provides
    UploadProductRepository provideUploadProductRepository(UploadProductDataSource uploadProductDataSource){
        return new UploadProductRepositoryImpl(uploadProductDataSource);
    }

    @AddProductServiceScope
    @Provides
    ImageProductUploadRepository provideImageProductUploadRepository(ImageProductUploadDataSource iageProductUploadDataSource){
        return new ImageProductUploadRepositoryImpl(iageProductUploadDataSource);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @AddProductServiceScope
    @Provides
    UploadProductApi provideUploadProductApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(UploadProductApi.class);
    }

}
