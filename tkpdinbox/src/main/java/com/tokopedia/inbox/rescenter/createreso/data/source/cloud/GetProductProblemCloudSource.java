package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetProductProblemCloudSource {
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private ResolutionApi resolutionApi;

    public GetProductProblemCloudSource(Context context,
                                        GetProductProblemMapper productProblemMapper,
                                        ResolutionApi resolutionApi) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<ProductProblemResponseDomain> getProductProblemList(RequestParams requestParams) {
        return resolutionApi.getProductProblemList(
                requestParams.getString(GetProductProblemUseCase.ORDER_ID, ""),
                requestParams.getParameters())
                .map(productProblemMapper);
    }
}
