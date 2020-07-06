package com.tokopedia.seller.product.picker.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.seller.product.picker.view.mapper.GetProductListMapper;
import com.tokopedia.seller.product.picker.view.presenter.ProductListPickerSearchPresenter;
import com.tokopedia.seller.product.picker.view.presenter.ProductListPickerSearchPresenterImpl;
import com.tokopedia.seller.shop.common.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.shop.common.domain.interactor.GetProductListUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.GQL_PRODUCT_LIST;

/**
 * Created by zulfikarrahman on 9/8/17.
 */

@Module
@ProductListScope
public class ProductListModule {

    @ProductListScope
    @Provides
    ProductListPickerSearchPresenter provideListPickerSearchPresenter(GetProductListUseCase getProductListUseCase,
                                                                      GetProductListMapper getProductListMapper,
                                                                      UserSessionInterface userSession){
        return new ProductListPickerSearchPresenterImpl(getProductListUseCase, getProductListMapper, userSession);
    }

    @ProductListScope
    @Provides
    public AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context);
        }else{
            return null;
        }
    }

    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }

    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor();
    }

    @Provides
    public HeaderErrorResponseInterceptor provideHeaderErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                                      AbstractionRouter abstractionRouter) {
        return new GMAuthInterceptor(context, abstractionRouter);
    }

    @ProductListGMFeaturedQualifier
    @Provides
    public OkHttpClient provideGMOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                              @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                              HeaderErrorResponseInterceptor errorResponseInterceptor,
                                              CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ProductListGMFeaturedQualifier
    @ProductListScope
    @Provides
    public Retrofit provideGMRetrofit(@ProductListGMFeaturedQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ProductListScope
    @Provides
    public GMCommonApi provideGMCommonApi(@ProductListGMFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(GMCommonApi.class);
    }

    @ProductListScope
    @Provides
    public GMCommonCloudDataSource provideGMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        return new GMCommonCloudDataSource(gmCommonApi);
    }

    @ProductListScope
    @Provides
    public GMCommonDataSource provideGMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        return new GMCommonDataSource(gmCommonCloudDataSource);
    }

    @ProductListScope
    @Provides
    public GMCommonRepository provideGMCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    @ProductListScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ProductListScope
    @Provides
    @Named(GQL_PRODUCT_LIST)
    public String provideProductListQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                com.tokopedia.shop.common.R.raw.gql_get_product_list
        );
    }
}
