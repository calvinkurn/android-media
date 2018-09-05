package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class PostAppealSolutionUseCase extends UseCase<EditAppealResolutionSolutionDomain> {
    public static final String RESO_ID = "reso_id";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_REFUND_AMOUNT = "refund_amount";
    public static final String PARAM_RESULT = "result";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public PostAppealSolutionUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<EditAppealResolutionSolutionDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.postAppealSolution(requestParams);
    }

    public static RequestParams postAppealSolutionUseCaseParams(String resoId,
                                                                int solutionId,
                                                                long refundAmount) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_SOLUTION, solutionId);
        params.putLong(PARAM_REFUND_AMOUNT, refundAmount);
        params.putString(RESO_ID, resoId);
        return params;
    }

    public static RequestParams postAppealSolutionUseCaseParamsWithoutRefund(String resoId,
                                                                             int solutionId) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_SOLUTION, solutionId);
        params.putString(RESO_ID, resoId);
        return params;
    }

    public static RequestParams postAppealSolution(EditAppealSolutionModel model) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_RESULT, model.writeToJson());
        params.putString(RESO_ID, model.resolutionId);
        return params;
    }

}
