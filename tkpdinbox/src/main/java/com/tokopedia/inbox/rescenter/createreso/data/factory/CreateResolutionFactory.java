package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetSolutionCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionFactory {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private SolutionMapper solutionMapper;
    private ResolutionApi resolutionApi;

    public CreateResolutionFactory(Context context,
                                   GetProductProblemMapper productProblemMapper,
                                   SolutionMapper solutionMapper,
                                   ResolutionApi resolutionApi) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.solutionMapper = solutionMapper;
        this.resolutionApi = resolutionApi;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, resolutionApi);
    }

    public GetSolutionCloudSource getSolutionCloudSource() {
        return new GetSolutionCloudSource(context, solutionMapper, resolutionApi);
    }

}
