package com.tokopedia.ride.completetrip.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by alvarisi on 3/31/17.
 */

public class CompleteTripPresenter extends BaseDaggerPresenter<CompleteTripContract.View>
        implements CompleteTripContract.Presenter {
    private GetReceiptUseCase getReceiptUseCase;
    private GetCurrentDetailRideRequestUseCase getCurrentDetailRideRequestUseCase;

    public CompleteTripPresenter(GetReceiptUseCase getReceiptUseCase, GetCurrentDetailRideRequestUseCase getCurrentDetailRideRequestUseCase) {
        this.getReceiptUseCase = getReceiptUseCase;
        this.getCurrentDetailRideRequestUseCase = getCurrentDetailRideRequestUseCase;
    }

    @Override
    public void actionGetReceipt() {
        getView().showGetReceiptLoading();
        getReceiptUseCase.execute(getView().getReceiptParam(), new Subscriber<Receipt>() {
            @Override
            public void onCompleted() {
                getView().hideGetReceiptLoading();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideGetReceiptLoading();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onNext(Receipt receipt) {
                getView().renderReceipt(receipt);
            }
        });
    }
}
