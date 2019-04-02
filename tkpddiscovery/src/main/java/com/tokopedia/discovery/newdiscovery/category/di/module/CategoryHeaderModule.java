package com.tokopedia.discovery.newdiscovery.category.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.newdiscovery.category.data.mapper.CategoryHeaderMapper;
import com.tokopedia.discovery.newdiscovery.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.category.data.source.CategoryDataSource;
import com.tokopedia.discovery.newdiscovery.category.di.scope.CategoryScope;
import com.tokopedia.discovery.newdiscovery.category.domain.CategoryRepository;
import com.tokopedia.discovery.newdiscovery.category.domain.usecase.GetCategoryHeaderUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alifa on 10/30/17.
 */

@Module
public class CategoryHeaderModule {

    @Provides
    CategoryHeaderMapper categoryHeaderMapper() {
        return new CategoryHeaderMapper();
    }

    @Provides
    CategoryDataSource categoryDataSource(HadesApi hadesApi, CategoryHeaderMapper categoryHeaderMapper) {
        return new CategoryDataSource(hadesApi,categoryHeaderMapper);
    }

    @Provides
    CategoryRepository categoryRepository(CategoryDataSource categoryDataSource) {
        return new CategoryRepositoryImpl(categoryDataSource);
    }

    @Provides
    GetCategoryHeaderUseCase getCategoryHeaderUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread, CategoryRepository categoryRepository){
        return new GetCategoryHeaderUseCase(threadExecutor, postExecutionThread, categoryRepository);
    }

}
