package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.resolutioncenter.createreso.service.CreateResoService;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemFactory {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private CreateResoService createResoService;

    public ProductProblemFactory(Context context, GetProductProblemMapper productProblemMapper, CreateResoService createResoService) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.createResoService = createResoService;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, createResoService);
    }

}
