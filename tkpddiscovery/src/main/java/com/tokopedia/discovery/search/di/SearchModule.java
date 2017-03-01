package com.tokopedia.discovery.search.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.core.base.di.qualifier.AceQualifier;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.domain.interactor.DeleteSearchUseCase;
import com.tokopedia.discovery.search.domain.interactor.SearchDataFactory;
import com.tokopedia.discovery.search.domain.interactor.SearchUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author erry on 23/02/17.
 */

@Module
public class SearchModule {

    @SearchScope
    @Provides
    AceService provideAceService(@AceQualifier Retrofit retrofit){
        return retrofit.create(AceService.class);
    }

    @SearchScope
    @Provides
    SearchDataFactory provideSearchFactory(@ActivityContext Context context, Gson gson,
                                           AceService aceService){
        return new SearchDataFactory(context, gson, aceService);
    }

    @SearchScope
    @Provides
    SearchPresenter provideSearchPresenter(@ActivityContext Context context,
                                           SearchUseCase searchUseCase,
                                           DeleteSearchUseCase deleteSearchUseCase){
        return new SearchPresenter(context, searchUseCase, deleteSearchUseCase);
    }

    @SearchScope
    @Provides
    SearchUseCase provideSearchUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       SearchDataFactory searchFactory){
        return new SearchUseCase(threadExecutor,
                postExecutionThread, searchFactory);
    }

    @SearchScope
    @Provides
    DeleteSearchUseCase provideDeleteSearchUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   SearchDataFactory searchDataFactory){
        return new DeleteSearchUseCase(threadExecutor, postExecutionThread, searchDataFactory);
    }
}
