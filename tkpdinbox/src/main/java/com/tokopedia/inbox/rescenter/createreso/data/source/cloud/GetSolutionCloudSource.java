package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetSolutionCloudSource {
    private Context context;
    private SolutionMapper solutionMapper;
    private ResolutionApi resolutionApi;

    public GetSolutionCloudSource(Context context,
                                  SolutionMapper solutionMapper,
                                  ResolutionApi resolutionApi) {
        this.context = context;
        this.solutionMapper = solutionMapper;
        this.resolutionApi = resolutionApi;
    }

    public Observable<SolutionResponseDomain> getSolutionResponse(RequestParams requestParams) {
        try {
            return resolutionApi.getSolution(requestParams.getString(GetSolutionUseCase.ORDER_ID, ""),
                    requestParams.getString(GetSolutionUseCase.PARAM_PROBLEM, ""))
                    .map(solutionMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
