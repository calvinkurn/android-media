package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.google.gson.JsonObject;
import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetSolutionUseCase extends UseCase<SolutionResponseDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";
    public static final String PARAM_PROBLEM = "problem";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public GetSolutionUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<SolutionResponseDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.getSolution(requestParams);
    }

    public RequestParams getSolutionUseCaseParams(ResultViewModel resultViewModel) {
        JsonObject problemObject = new JsonObject();
        try {
            problemObject.add(PARAM_PROBLEM, resultViewModel.getProblemArray());
            if (resultViewModel.resolutionId != null) {
                problemObject.addProperty(PARAM_RESOLUTION_ID, Integer.valueOf(resultViewModel.resolutionId));
            }
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, resultViewModel.orderId);
            params.putObject(PARAM_PROBLEM, problemObject);

            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
