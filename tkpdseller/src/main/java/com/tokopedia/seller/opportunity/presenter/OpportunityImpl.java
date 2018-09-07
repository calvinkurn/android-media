package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityNewPriceUseCase;
import com.tokopedia.seller.opportunity.presenter.subscriber.AcceptOpportunitySubscriber;

import rx.Subscriber;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OpportunityImpl extends OpportunityPresenter {

    private AcceptReplacementUseCase acceptReplacementUseCase;
    private GetOpportunityNewPriceUseCase newPriceUseCase;

    public OpportunityImpl(AcceptReplacementUseCase acceptReplacementUseCase,
                           GetOpportunityNewPriceUseCase newPriceUseCase) {
        this.acceptReplacementUseCase = acceptReplacementUseCase;
        this.newPriceUseCase = newPriceUseCase;
    }

    @Override
    public void getNewPriceInfo() {
        newPriceUseCase.execute(getNewPriceInfoParams(), new Subscriber<OpportunityNewPriceData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(!isViewAttached())
                    return;

                getView().onErrorPriceInfo(ErrorHandler.getErrorMessage(e));
                getView().onErrorTakeOpportunity(ErrorHandler.getErrorMessage(e));
            }

            @Override
            public void onNext(OpportunityNewPriceData opportunityNewPriceData) {
                if(!isViewAttached())
                    return;

                getView().onSuccessNewPrice(opportunityNewPriceData);
            }
        });
    }

    @Override
    public void acceptOpportunity() {
        getView().showLoadingProgress();
        acceptReplacementUseCase.execute(getAcceptOpportunityParams(),
                new AcceptOpportunitySubscriber(getView()));
    }

    private RequestParams getNewPriceInfoParams(){
        return GetOpportunityNewPriceUseCase.createRequestParams(getView().getOpportunityId());
    }

    private RequestParams getAcceptOpportunityParams() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptReplacementUseCase.PARAMS_ID, getView().getOpportunityId());
        return params;
    }


    @Override
    public void unsubscribeObservable() {
        acceptReplacementUseCase.unsubscribe();
    }


}
