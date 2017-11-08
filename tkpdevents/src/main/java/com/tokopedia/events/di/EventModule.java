package com.tokopedia.events.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.events.data.EventRepositoryData;
import com.tokopedia.events.data.EventsDataStoreFactory;
import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
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


    @EventScope
    @EventQualifier
    @Provides
    OkHttpClient provideOkhttpDefaultAuth(OkHttpRetryPolicy okHttpRetryPolicy) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDefaultAuth();
    }






}
