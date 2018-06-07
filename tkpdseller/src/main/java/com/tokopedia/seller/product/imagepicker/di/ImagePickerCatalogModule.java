package com.tokopedia.seller.product.imagepicker.di;

import com.tokopedia.seller.product.imagepicker.data.source.CatalogApi;
import com.tokopedia.seller.product.imagepicker.data.source.CatalogImageDataSource;
import com.tokopedia.seller.product.imagepicker.domain.CatalogImageRepository;
import com.tokopedia.seller.product.imagepicker.data.repository.CatalogImageRepositoryImpl;
import com.tokopedia.seller.product.imagepicker.domain.interactor.GetCatalogImageUseCase;
import com.tokopedia.seller.product.imagepicker.view.presenter.ImagePickerCatalogPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

@CatalogImageScope
@Module
public class ImagePickerCatalogModule {

    @CatalogImageScope
    @Provides
    ImagePickerCatalogPresenter provideImagePickerCatalogPresenter(GetCatalogImageUseCase getCatalogImageUseCase){
        return new ImagePickerCatalogPresenter(getCatalogImageUseCase);
    }

    @CatalogImageScope
    @Provides
    CatalogImageRepository provideCatalogImageRepository(CatalogImageDataSource catalogImageDataSource){
        return new CatalogImageRepositoryImpl(catalogImageDataSource);
    }

    @CatalogImageScope
    @Provides
    CatalogApi provideCatalogApi(Retrofit.Builder retrofit){
        return retrofit.build().create(CatalogApi.class);
    }
}
