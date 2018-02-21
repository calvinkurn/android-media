package com.tokopedia.seller.product.edit.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.base.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.seller.base.data.source.UploadImageDataSource;
import com.tokopedia.seller.base.domain.UploadImageRepository;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.repository.GenerateHostRepositoryImpl;
import com.tokopedia.seller.product.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.edit.data.repository.ProductRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.product.edit.data.source.GenerateHostDataSource;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.edit.data.source.ProductDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.di.scope.AddProductServiceScope;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.uploadproduct.UploadProductUseCase;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServicePresenter;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServicePresenterImpl;
import com.tokopedia.seller.product.variant.data.source.ProductVariantDataSource;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepositoryImpl;

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
    AddProductServicePresenter provideAddProductServicePresenter(UploadProductUseCase uploadProductUseCase, UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        return new AddProductServicePresenterImpl(uploadProductUseCase, updateUploadingDraftProductUseCase);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource){
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @AddProductServiceScope
    @Provides
    ProductRepository provideUploadProductRepository(ProductDataSource productDataSource,
                                                     FetchVideoEditProductDataSource fetchVideoEditProductDataSource){
        return new ProductRepositoryImpl(productDataSource, fetchVideoEditProductDataSource);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @AddProductServiceScope
    @Provides
    ProductVariantRepository productVariantRepository(ProductVariantDataSource productVariantDataSource){
        return new ProductVariantRepositoryImpl(productVariantDataSource);
    }

    @AddProductServiceScope
    @Provides
    GoldMerchantService productGoldMerchantService(){
        return new GoldMerchantService();
    }

    @AddProductServiceScope
    @Provides
    UploadImageUseCase<UploadImageModel> provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   UploadImageRepository uploadImageRepository,
                                                                   GenerateHostRepository generateHostRepository,
                                                                   Gson gson,
                                                                   NetworkCalculator networkCalculator){
        return new UploadImageUseCase<UploadImageModel>(threadExecutor, postExecutionThread, uploadImageRepository,
                generateHostRepository,gson, networkCalculator, UploadImageModel.class);
    }

    @AddProductServiceScope
    @Provides
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource){
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }

    @AddProductServiceScope
    @Provides
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context,
            UploadImageDataSource uploadImageDataSource){
        return new NetworkCalculator(NetworkConfig.POST, context, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL).setIdentity().compileAllParam().finish();
    }

}
