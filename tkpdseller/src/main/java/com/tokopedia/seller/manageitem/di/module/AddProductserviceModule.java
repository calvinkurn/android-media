package com.tokopedia.seller.manageitem.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.seller.manageitem.common.mapper.ProductUploadMapper;
import com.tokopedia.seller.manageitem.data.cloud.api.GenerateHostApi;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDB;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDao;
import com.tokopedia.seller.manageitem.data.model.UploadImageModel;
import com.tokopedia.seller.manageitem.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.manageitem.data.source.GenerateHostDataSource;
import com.tokopedia.seller.manageitem.data.source.ProductDataSource;
import com.tokopedia.seller.manageitem.data.source.ProductDraftDataSource;
import com.tokopedia.seller.manageitem.data.source.ProductVariantDataSource;
import com.tokopedia.seller.manageitem.data.source.UploadImageDataSource;
import com.tokopedia.seller.manageitem.di.scope.AddProductServiceScope;
import com.tokopedia.seller.manageitem.domain.repository.GenerateHostRepository;
import com.tokopedia.seller.manageitem.domain.repository.GenerateHostRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.ProductRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.ProductVariantRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductVariantRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.repository.UploadImageRepository;
import com.tokopedia.seller.manageitem.domain.repository.UploadImageRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.usecase.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.FetchDraftProductUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.SubmitProductUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.manageitem.domain.usecase.UploadImageUseCase;
import com.tokopedia.seller.manageitem.view.presenter.AddProductServicePresenter;
import com.tokopedia.seller.manageitem.view.presenter.AddProductServicePresenterImpl;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    AddProductServicePresenter provideAddProductServicePresenter(FetchDraftProductUseCase fetchDraftProductUseCase,
                                                                 SubmitProductUseCase uploadProductUseCase,
                                                                 DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                                                 UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                                                 ProductUploadMapper productUploadMapper) {
        return new AddProductServicePresenterImpl(fetchDraftProductUseCase, uploadProductUseCase, deleteSingleDraftProductUseCase, updateUploadingDraftProductUseCase, productUploadMapper);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context) {
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource) {
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @AddProductServiceScope
    @Provides
    ProductRepository provideUploadProductRepository(ProductDataSource productDataSource,
                                                     FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        return new ProductRepositoryImpl(productDataSource, fetchVideoEditProductDataSource);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(GenerateHostApi.class);
    }

    @AddProductServiceScope
    @Provides
    ProductVariantRepository productVariantRepository(ProductVariantDataSource productVariantDataSource) {
        return new ProductVariantRepositoryImpl(productVariantDataSource);
    }

    @AddProductServiceScope
    @Provides
    GoldMerchantService productGoldMerchantService() {
        return new GoldMerchantService();
    }

    @AddProductServiceScope
    @Provides
    UploadImageUseCase<UploadImageModel> provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   UploadImageRepository uploadImageRepository,
                                                                   GenerateHostRepository generateHostRepository,
                                                                   Gson gson,
                                                                   NetworkCalculator networkCalculator) {
        return new UploadImageUseCase<UploadImageModel>(threadExecutor, postExecutionThread, uploadImageRepository,
                generateHostRepository, gson, networkCalculator, UploadImageModel.class);
    }

    @AddProductServiceScope
    @Provides
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource) {
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }

    @AddProductServiceScope
    @Provides
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context,
                                               UploadImageDataSource uploadImageDataSource) {
        return new NetworkCalculator(NetworkConfig.POST, context, TokopediaUrl.Companion.getInstance().getWEB()).setIdentity().compileAllParam().finish();
    }

    @AddProductServiceScope
    @Provides
    ProductUploadMapper provideProductUploadMapper() {
        return new ProductUploadMapper();
    }

    @AddProductServiceScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        return new FetchDraftProductUseCase(productDraftRepository);
    }

    @AddProductServiceScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB){
        return productDraftDB.getProductDraftDao();
    }
}