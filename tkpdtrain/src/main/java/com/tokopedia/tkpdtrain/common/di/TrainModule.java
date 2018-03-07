package com.tokopedia.tkpdtrain.common.di;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.common.constant.TrainUrl;
import com.tokopedia.tkpdtrain.common.data.TrainDataStoreFactory;
import com.tokopedia.tkpdtrain.common.data.TrainRepositoryImpl;
import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.data.TrainStationDataStoreFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by alvarisi on 2/19/18.
 */
@Module
public class TrainModule {
    public TrainModule() {
    }

    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        return new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();

    }

    @TrainScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TrainUrl.BASE_URL).client(okHttpClient).build();
    }

    @TrainScope
    @Provides
    public TrainApi provideTrainApi(Retrofit retrofit) {
        return retrofit.create(TrainApi.class);
    }

    @TrainScope
    @Provides
    public TrainDataStoreFactory provideDataStoreFactory(TrainApi trainApi) {
        return new TrainDataStoreFactory(trainApi);
    }
    @TrainScope
    @Provides
    public TrainStationDataStoreFactory provideTrainStationDataStoreFactory(TrainApi trainApi) {
        return new TrainStationDataStoreFactory(trainApi);
    }

    @TrainScope
    @Provides
    public TrainRepository provideTrainRepository(TrainDataStoreFactory trainDataStoreFactory, TrainStationDataStoreFactory trainStationDataStoreFactory) {
        return new TrainRepositoryImpl(trainDataStoreFactory, trainStationDataStoreFactory);
    }
}
