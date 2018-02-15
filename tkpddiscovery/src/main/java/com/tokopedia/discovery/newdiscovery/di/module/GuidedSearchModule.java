package com.tokopedia.discovery.newdiscovery.di.module;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.di.qualifier.AceGetGuidedSearchQualifier;
import com.tokopedia.discovery.newdiscovery.data.mapper.GuidedSearchMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.GuidedSearchRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.GuidedSearchRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.GuidedSearchDataSource;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetSearchGuideUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by henrypriyono on 14/02/18.
 */

@Module
public class GuidedSearchModule {

    @Provides
    GetSearchGuideUseCase getSearchGuideUseCase(
            GuidedSearchRepository guidedSearchRepository) {
        return new GetSearchGuideUseCase(guidedSearchRepository);
    }

    @Provides
    GuidedSearchRepository guidedSearchRepository(GuidedSearchDataSource guidedSearchDataSource){
        return new GuidedSearchRepositoryImpl(guidedSearchDataSource);
    }

    @Provides
    GuidedSearchDataSource guidedSearchDataSource(@AceGetGuidedSearchQualifier BrowseApi searchApi,
                                                  GuidedSearchMapper mapper) {
        return new GuidedSearchDataSource(searchApi, mapper);
    }

    @Provides
    GuidedSearchMapper guidedSearchMapper() {
        return new GuidedSearchMapper();
    }
}
