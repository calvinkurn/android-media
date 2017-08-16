package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.resolutioncenter.createreso.service.CreateResoService;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetProductProblemCloudSource {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private CreateResoService createResoService;

    public GetProductProblemCloudSource(Context context,
                                        GetProductProblemMapper productProblemMapper,
                                        CreateResoService createResoService) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.createResoService = createResoService;
    }

    public Observable<ProductProblemResponseDomain> getProductProblemList(RequestParams requestParams) {
       return createResoService.getApi().getProductProblem(requestParams.getInt(GetProductProblemUseCase.ORDER_ID, 0) +"/"+ TkpdBaseURL.ResCenterV5.PATH_STEP_1, new HashMap<String, String>())
               .map(productProblemMapper);
    }
}
