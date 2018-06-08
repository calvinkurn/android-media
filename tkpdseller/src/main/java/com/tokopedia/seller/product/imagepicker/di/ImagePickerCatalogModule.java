package com.tokopedia.seller.product.imagepicker.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.seller.product.imagepicker.data.source.CatalogApi;
import com.tokopedia.seller.product.imagepicker.data.source.CatalogImageDataSource;
import com.tokopedia.seller.product.imagepicker.domain.CatalogImageRepository;
import com.tokopedia.seller.product.imagepicker.data.repository.CatalogImageRepositoryImpl;
import com.tokopedia.seller.product.imagepicker.domain.interactor.GetCatalogImageUseCase;
import com.tokopedia.seller.product.imagepicker.util.CatalogConstant;
import com.tokopedia.seller.product.imagepicker.view.presenter.ImagePickerCatalogPresenter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    CatalogApi provideCatalogApi(Retrofit.Builder retrofit, @CatalogQualifier OkHttpClient okHttpClient){
        return retrofit
                .client(okHttpClient)
                .baseUrl(CatalogConstant.URL_HADES)
                .build()
                .create(CatalogApi.class);
    }

    @CatalogImageScope
    @CatalogQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        if(GlobalConfig.isAllowDebuggingTools()){
            builder.addInterceptor(new HttpLoggingInterceptor());
            builder.addInterceptor(new ChuckInterceptor(context));
        }
        return builder.build();
    }
}
