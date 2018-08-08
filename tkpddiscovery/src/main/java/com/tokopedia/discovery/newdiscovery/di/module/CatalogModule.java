package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.discovery.newdiscovery.data.repository.CatalogRepository;
import com.tokopedia.discovery.newdiscovery.data.mapper.BrowseCatalogMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.CatalogRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.CatalogDataSource;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetBrowseCatalogLoadMoreUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetBrowseCatalogUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 10/12/17.
 */
@Module
public class CatalogModule {

    @Provides
    CatalogRepository catalogRepository(CatalogDataSource catalogDataSource) {
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @Provides
    GetBrowseCatalogUseCase getBrowseCatalogUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CatalogRepository catalogRepository
    ) {
        return new GetBrowseCatalogUseCase(
                threadExecutor,
                postExecutionThread,
                catalogRepository
        );
    }

    @Provides
    CatalogDataSource catalogDataSource(BrowseApi browseApi,
                                        BrowseCatalogMapper browseCatalogMapper) {
        return new CatalogDataSource(browseApi, browseCatalogMapper);
    }

    @Provides
    BrowseCatalogMapper browseCatalogMapper(Gson gson) {
        return new BrowseCatalogMapper(gson);
    }

    @Provides
    GetBrowseCatalogLoadMoreUseCase getBrowseCatalogLoadMoreUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CatalogRepository catalogRepository
    ) {
        return new GetBrowseCatalogLoadMoreUseCase(
                threadExecutor,
                postExecutionThread,
                catalogRepository
        );
    }

    @Provides
    CatalogPresenter catalogPresenter(@ApplicationContext Context context) {
        return new CatalogPresenter(context);
    }
}
