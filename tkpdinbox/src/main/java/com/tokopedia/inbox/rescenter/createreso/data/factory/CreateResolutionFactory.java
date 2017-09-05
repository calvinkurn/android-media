package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep1Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep2Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateResoStep1CloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateResoStep2CloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetSolutionCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionFactory {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private SolutionMapper solutionMapper;
    private CreateResoStep1Mapper createResoStep1Mapper;
    private CreateResoStep2Mapper createResoStep2Mapper;
    private ResolutionApi resolutionApi;

    public CreateResolutionFactory(Context context,
                                   GetProductProblemMapper productProblemMapper,
                                   SolutionMapper solutionMapper,
                                   CreateResoStep1Mapper createResoStep1Mapper,
                                   CreateResoStep2Mapper createResoStep2Mapper,
                                   ResolutionApi resolutionApi) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.solutionMapper = solutionMapper;
        this.createResoStep1Mapper = createResoStep1Mapper;
        this.createResoStep2Mapper = createResoStep2Mapper;
        this.resolutionApi = resolutionApi;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, resolutionApi);
    }

    public GetSolutionCloudSource getSolutionCloudSource() {
        return new GetSolutionCloudSource(context, solutionMapper, resolutionApi);
    }

    public CreateResoStep1CloudSource createResoStep1CloudSource() {
        return new CreateResoStep1CloudSource(context, createResoStep1Mapper, resolutionApi);
    }

    public CreateResoStep2CloudSource createResoStep2CloudSource() {
        return new CreateResoStep2CloudSource(context, createResoStep2Mapper, resolutionApi);
    }

}
