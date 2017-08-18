package com.tokopedia.inbox.rescenter.createreso.view.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.createreso.data.factory.ProductProblemFactory;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;


import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by yoasfs on 11/08/17.
 */


@Module
public class CreateResoModule {

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    public CreateResoModule() {

    }



    @CreateResoScope
    @Provides
    ResolutionApi provideResolutionService(@ResolutionQualifier Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

    @CreateResoScope
    @Provides
    GetProductProblemMapper provideGetProductProblemMapper() {
        return new GetProductProblemMapper(new Gson());
    }

    @CreateResoScope
    @Provides
    ProductProblemFactory provideProductProblemFactory(@ActivityContext Context context,
                                                       GetProductProblemMapper mapper,
                                                       ResolutionApi resolutionApi) {
        return new ProductProblemFactory(context, mapper, resolutionApi);
    }

    @CreateResoScope
    @Provides
    ProductProblemRepository provideProductProblemRepository(ProductProblemFactory productProblemFactory) {
        return new ProductProblemRepositoryImpl(productProblemFactory);
    }

    @CreateResoScope
    @Provides
    GetProductProblemUseCase provideGetProductProblemUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ProductProblemRepository productProblemRepository) {
        return new GetProductProblemUseCase(threadExecutor,
                postExecutionThread,
                productProblemRepository);
    }


}
