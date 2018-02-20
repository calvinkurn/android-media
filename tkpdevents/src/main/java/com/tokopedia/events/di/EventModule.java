package com.tokopedia.events.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.data.EventRepositoryData;
import com.tokopedia.events.data.EventsDataStoreFactory;
import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetEventsLocationListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.domain.postusecase.PostVerifyCartUseCase;

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

    Context thisContext;

    public EventModule(Context context) {
        thisContext = context;
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

        return OkHttpFactory.create().buildDaggerClientBearerEvents(
                eventInerceptors,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                loggingInterceptor
        );

    }


    @EventScope
    @Provides
    EventInerceptors provideEventInterCeptor(Context context) {
        String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new EventInerceptors(oAuthString, context);
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

    @Provides
    @EventScope
    GetEventDetailsRequestUseCase provideGetEventDetailsRequestUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       EventRepository eventRepository) {
        return new GetEventDetailsRequestUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    GetSearchEventsListRequestUseCase provideGetSearchEventsListRequestUseCase(ThreadExecutor threadExecutor,
                                                                               PostExecutionThread postExecutionThread,
                                                                               EventRepository eventRepository) {
        return new GetSearchEventsListRequestUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    PostValidateShowUseCase providesPostValidateShowUseCase(ThreadExecutor threadExecutor,
                                                            PostExecutionThread postExecutionThread,
                                                            EventRepository eventRepository) {
        return new PostValidateShowUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    PostVerifyCartUseCase providesPostVerifyCartUseCase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        EventRepository eventRepository) {
        return new PostVerifyCartUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    GetEventSeatLayoutUseCase providesGetEventSeatLayoutUseCase(ThreadExecutor threadExecutor,
                                                                PostExecutionThread postExecutionThread,
                                                                EventRepository eventRepository) {
        return new GetEventSeatLayoutUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    GetSearchNextUseCase providesGetSearchNextUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread,
                                                      EventRepository eventRepository) {
        return new GetSearchNextUseCase(threadExecutor, postExecutionThread, eventRepository);
    }

    @Provides
    @EventScope
    Context getActivityContext() {
        return thisContext;
    }

    @Provides
    @EventScope
    ProfileSourceFactory providesProfileSourceFactory(Context context,SessionHandler sessionHandler) {
        return new ProfileSourceFactory(context,
                new PeopleService(),
                new ProfileMapper(),
                new GlobalCacheManager(),
                new AnalyticsCacheHandler(),
                sessionHandler);
    }

    @Provides
    @EventScope
    ProfileRepository providesProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @Provides
    @EventScope
    ProfileUseCase providesProfileUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ProfileRepository profileRepository) {
        return new ProfileUseCase(
                threadExecutor, postExecutionThread, profileRepository);
    }


}
