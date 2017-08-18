package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemFactory {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private ResolutionApi resolutionApi;

    public ProductProblemFactory(Context context, GetProductProblemMapper productProblemMapper, ResolutionApi resolutionApi) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.resolutionApi = resolutionApi;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, resolutionApi);
    }

}
