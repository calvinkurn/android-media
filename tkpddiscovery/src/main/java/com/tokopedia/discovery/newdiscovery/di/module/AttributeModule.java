package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.newdiscovery.data.mapper.DynamicAttributeMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicAutoSelectedFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterV4UseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetHotListFilterValueUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.data.mapper.HotlistAttributeMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.discovery.newdiscovery.data.source.AttributeDataSource;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistAttributeUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;

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
    GetHotlistAttributeUseCase getHotlistAttributeUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AttributeRepository attributeRepository) {
        return new GetHotlistAttributeUseCase(threadExecutor, postExecutionThread, attributeRepository);
    }

    @Provides
    GetDynamicFilterUseCase getDynamicFilterUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AttributeRepository attributeRepository) {
        return new GetDynamicFilterUseCase(threadExecutor, postExecutionThread, attributeRepository);
    }

    @Provides
    GetHotListFilterValueUseCase getHotListFilterValueUseCase(
            @ApplicationContext Context context,
            GraphqlUseCase graphqlUseCase) {
        return new GetHotListFilterValueUseCase(context, graphqlUseCase);
    }

    @Provides
    GetDynamicAutoSelectedFilterUseCase getDyanamicAutoSelecetedFilterUseCase(GetHotListFilterValueUseCase getHotListFilterValueUseCase,
                                                                                GetDynamicFilterUseCase getDynamicFilterUseCase){
        return new GetDynamicAutoSelectedFilterUseCase(getHotListFilterValueUseCase,getDynamicFilterUseCase);
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
                                            HotlistAttributeMapper hotlistAttributeMapper,
                                            DynamicAttributeMapper dynamicAttributeMapper) {
        return new AttributeDataSource(attributeApi, hotlistAttributeMapper, dynamicAttributeMapper);
    }

    @Provides
    HotlistAttributeMapper hotlistAttributeMapper(Gson gson) {
        return new HotlistAttributeMapper(gson);
    }

    @Provides
    DynamicAttributeMapper dynamicAttributeMapper(Gson gson) {
        return new DynamicAttributeMapper(gson);
    }
}
