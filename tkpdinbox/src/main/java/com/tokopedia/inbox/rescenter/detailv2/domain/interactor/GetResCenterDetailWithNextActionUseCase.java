package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/9/17.
 */

public class GetResCenterDetailWithNextActionUseCase extends UseCase<DetailResCenter> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;
    GetResCenterDetailUseCase resCenterDetailUseCase;
    private GetNextActionUseCase nextActionUseCase;

    public GetResCenterDetailWithNextActionUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   ResCenterRepository resCenterRepository,
                                                   GetResCenterDetailUseCase resCenterDetailUseCase,
                                                   GetNextActionUseCase nextActionUseCase) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
        this.resCenterDetailUseCase = resCenterDetailUseCase;
        this.nextActionUseCase = nextActionUseCase;
    }

    @Override
    public Observable<DetailResCenter> createObservable(RequestParams requestParams) {
        DetailResCenter detailResCenter = new DetailResCenter();
        return getObservableDetailReso(requestParams, detailResCenter)
                .flatMap(getNextActionObservable(requestParams))
                .flatMap(addNextActionDomainToTemp(detailResCenter));
    }

    private Observable<DetailResCenter> getObservableDetailReso(
            RequestParams requestParams, final DetailResCenter detailResCenter) {
        return resCenterDetailUseCase.createObservable(requestParams)
                .flatMap(addResultToTemp(detailResCenter));
    }

    private Func1<DetailResCenter, Observable<DetailResCenter>>
    addResultToTemp(final DetailResCenter detailResCenter) {
        return new Func1<DetailResCenter, Observable<DetailResCenter>>() {
            @Override
            public Observable<DetailResCenter> call(
                    DetailResCenter tempDetailResCenter) {
                detailResCenter.setSuccess(tempDetailResCenter.isSuccess());
                detailResCenter.setErrorCode(tempDetailResCenter.getErrorCode());
                detailResCenter.setAddress(tempDetailResCenter.getAddress());
                detailResCenter.setButton(tempDetailResCenter.getButton());
                detailResCenter.setMessageError(tempDetailResCenter.getMessageError());
                detailResCenter.setProductData(tempDetailResCenter.getProductData());
                detailResCenter.setResolution(tempDetailResCenter.getResolution());
                detailResCenter.setResolutionHistory(tempDetailResCenter.getResolutionHistory());
                detailResCenter.setShipping(tempDetailResCenter.getShipping());
                detailResCenter.setSolutionData(tempDetailResCenter.getSolutionData());
                return Observable.just(detailResCenter);
            }
        };
    }

    private Func1<DetailResCenter, Observable<NextActionDomain>> getNextActionObservable(final  RequestParams requestParams) {
        return new Func1<DetailResCenter, Observable<NextActionDomain>>() {
            @Override
            public Observable<NextActionDomain> call(DetailResCenter detailResCenter) {
                return nextActionUseCase.createObservable(requestParams);
            }
        };
    }

    private Func1<NextActionDomain, Observable<DetailResCenter>>
    addNextActionDomainToTemp(final DetailResCenter detailResCenter) {
        return new Func1<NextActionDomain, Observable<DetailResCenter>>() {
            @Override
            public Observable<DetailResCenter> call(NextActionDomain nextActionDomain) {
                detailResCenter.setNextAction(nextActionDomain);
                return Observable.just(detailResCenter);
            }
        };
    }

}
