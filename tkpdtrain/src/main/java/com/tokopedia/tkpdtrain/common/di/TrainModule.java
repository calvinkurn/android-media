package com.tokopedia.tkpdtrain.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.common.constant.TrainUrl;
import com.tokopedia.tkpdtrain.common.data.TrainDataStoreFactory;
import com.tokopedia.tkpdtrain.common.data.TrainRepositoryImpl;
import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleCacheDataStore;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleCloudDataStore;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleDbDataStore;
import com.tokopedia.tkpdtrain.station.data.TrainStationCacheDataStore;
import com.tokopedia.tkpdtrain.station.data.TrainStationCloudDataStore;
import com.tokopedia.tkpdtrain.station.data.TrainStationDataStoreFactory;
import com.tokopedia.tkpdtrain.station.data.TrainStationDbDataStore;

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
    public TrainStationDbDataStore provideTrainStationDbDataStore() {
        return new TrainStationDbDataStore();
    }

    @TrainScope
    @Provides
    public TrainStationCloudDataStore provideTrainStationCloudDataStore(TrainApi trainApi) {
        return new TrainStationCloudDataStore(trainApi);
    }

    @TrainScope
    @Provides
    public TrainStationCacheDataStore provideTrainStationCacheDataStore(@ApplicationContext Context context) {
        return new TrainStationCacheDataStore(context);
    }

    @TrainScope
    @Provides
    public TrainStationDataStoreFactory provideTrainStationDataStoreFactory(TrainStationDbDataStore trainStationDbDataStore,
                                                                            TrainStationCloudDataStore trainStationCloudDataStore,
                                                                            TrainStationCacheDataStore trainStationCacheDataStore) {
        return new TrainStationDataStoreFactory(trainStationDbDataStore, trainStationCloudDataStore, trainStationCacheDataStore);
    }

    @TrainScope
    @Provides
    public TrainRepository provideTrainRepository(TrainDataStoreFactory trainDataStoreFactory,
                                                  TrainStationDataStoreFactory trainStationDataStoreFactory,
                                                  TrainScheduleDataStoreFactory scheduleDataStoreFactory) {
        return new TrainRepositoryImpl(trainDataStoreFactory, trainStationDataStoreFactory, scheduleDataStoreFactory);
    }

    @TrainScope
    @Provides
    public TrainScheduleDbDataStore provideTrainScheduleDbDataStore() {
        return new TrainScheduleDbDataStore();
    }

    @TrainScope
    @Provides
    public TrainScheduleCloudDataStore provideTrainScheduleCloudDataStore(TrainApi trainApi) {
        return new TrainScheduleCloudDataStore(trainApi);
    }

    @TrainScope
    @Provides
    public TrainScheduleCacheDataStore provideTrainScheduleCacheDataStore(@ApplicationContext Context context) {
        return new TrainScheduleCacheDataStore(context);
    }

    @TrainScope
    @Provides
    public TrainScheduleDataStoreFactory provideTrainScheduleDataStoreFactory(TrainScheduleDbDataStore trainScheduleDbDataStore,
                                                                              TrainScheduleCloudDataStore trainScheduleCloudDataStore,
                                                                              TrainScheduleCacheDataStore trainScheduleCacheDataStore) {
        return new TrainScheduleDataStoreFactory(trainScheduleDbDataStore, trainScheduleCacheDataStore, trainScheduleCloudDataStore);
    }
}
