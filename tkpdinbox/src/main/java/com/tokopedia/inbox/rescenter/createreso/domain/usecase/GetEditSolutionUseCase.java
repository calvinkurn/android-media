package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetEditSolutionUseCase extends UseCase<EditSolutionResponseDomain> {
    public static final String RESO_ID = "reso_id";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public GetEditSolutionUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<EditSolutionResponseDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.getEditSolution(requestParams);
    }

    public RequestParams getEditSolutionUseCaseParams(String resoId) {
        RequestParams params = RequestParams.create();
        params.putString(RESO_ID, resoId);
        return params;
    }

}
