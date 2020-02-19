package com.tokopedia.discovery.newdiscovery.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.newdiscovery.data.mapper.DynamicAttributeMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterV4UseCase;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.discovery.newdiscovery.data.source.AttributeDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class AttributeModule {

    @Provides
    AttributeRepository attributeRepository(AttributeDataSource attributeDataSource){
        return new AttributeRepositoryImpl(attributeDataSource);
    }


    @Provides
    GetDynamicFilterUseCase getDynamicFilterUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AttributeRepository attributeRepository) {
        return new GetDynamicFilterUseCase(threadExecutor, postExecutionThread, attributeRepository);
    }

    @Provides
    GetDynamicFilterV4UseCase getDynamicFilterV4UseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AttributeRepository attributeRepository) {
        return new GetDynamicFilterV4UseCase(threadExecutor, postExecutionThread, attributeRepository);
    }

    @Provides
    AttributeDataSource attributeDataSource(BrowseApi attributeApi,
                                            DynamicAttributeMapper dynamicAttributeMapper) {
        return new AttributeDataSource(attributeApi, dynamicAttributeMapper);
    }

    @Provides
    DynamicAttributeMapper dynamicAttributeMapper(Gson gson) {
        return new DynamicAttributeMapper(gson);
    }
}
