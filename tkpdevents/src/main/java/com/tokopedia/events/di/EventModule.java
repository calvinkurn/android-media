package com.tokopedia.events.di;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.events.data.EventRepositoryData;
import com.tokopedia.events.data.EventsDataStoreFactory;
import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetEventsLocationListRequestUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@Module
public class EventModule {
    //private static final int CACHE_SIZE = 10485760;

   public EventModule(){

    }

    @Provides
    @EventScope
    EventsApi provideEventsApi(@EventQualifier Retrofit retrofit) {
        return retrofit.create(EventsApi.class);
    }

    @Provides
    @EventScope
    EventsDataStoreFactory provideEventsDataStoreFactory(EventsApi eventApi) {
        return new EventsDataStoreFactory(eventApi);
    }

    @Provides
    @EventScope
    EventRepository provideEventRepository(EventsDataStoreFactory eventsDataStoreFactory) {
        return new EventRepositoryData(eventsDataStoreFactory);
    }

    @Provides
    @EventScope
    GetEventsListRequestUseCase provideGetEventsListRequestUseCase(ThreadExecutor threadExecutor,
                                                              PostExecutionThread postExecutionThread,
                                                              EventRepository eventRepository) {
        return new GetEventsListRequestUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventQualifier
    @EventScope
    Retrofit provideEventRetrofit(@EventQualifier OkHttpClient okHttpClient,
                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.EVENTS_DOMAIN).client(okHttpClient).build();
    }

    @EventQualifier
    @Provides
    @EventScope
    OkHttpClient provideOkHttpClientEvent(EventInerceptors eventInerceptors,
                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                          ChuckInterceptor chuckInterceptor,
                                          DebugInterceptor debugInterceptor,
                                          HttpLoggingInterceptor loggingInterceptor) {

        return OkHttpFactory.create().buildDaggerClientBearerEventhailing(
                eventInerceptors,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                loggingInterceptor
        );

    }

    @Provides
    @EventScope
    EventInerceptors provideEventInterCeptor() {
        //String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new EventInerceptors();
    }

    @Provides
    @EventScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    @Provides
    @EventScope
    GetEventsLocationListRequestUseCase provideGetEventsLocationListRequestUseCase(ThreadExecutor threadExecutor,
                                                                           PostExecutionThread postExecutionThread,
                                                                           EventRepository eventRepository) {
        return new GetEventsLocationListRequestUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    GetEventsListByLocationRequestUseCase provideGetEventsListByLocationRequestUseCase(ThreadExecutor threadExecutor,
                                                                             PostExecutionThread postExecutionThread,
                                                                             EventRepository eventRepository) {
        return new GetEventsListByLocationRequestUseCase(threadExecutor, postExecutionThread, eventRepository);
    }
}
