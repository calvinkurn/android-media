package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetAppealSolutionUseCase extends UseCase<AppealSolutionResponseDomain> {
    public static final String RESO_ID = "reso_id";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public GetAppealSolutionUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<AppealSolutionResponseDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.getAppealSolutionResponse(requestParams);
    }

    public RequestParams getAppealSolutionUseCaseParams(String resoId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resoId);
        return params;
    }

}
