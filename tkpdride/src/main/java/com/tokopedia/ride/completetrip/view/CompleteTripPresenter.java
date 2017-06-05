package com.tokopedia.ride.completetrip.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/31/17.
 */

public class CompleteTripPresenter extends BaseDaggerPresenter<CompleteTripContract.View>
        implements CompleteTripContract.Presenter {
    private GetReceiptUseCase getReceiptUseCase;
    private GetRideRequestDetailUseCase getRideRequestDetailUseCase;
    private GiveDriverRatingUseCase giveDriverRatingUseCase;

    public CompleteTripPresenter(GetReceiptUseCase getReceiptUseCase,
                                 GetRideRequestDetailUseCase getRideRequestDetailUseCase,
                                 GiveDriverRatingUseCase giveDriverRatingUseCase) {
        this.getReceiptUseCase = getReceiptUseCase;
        this.getRideRequestDetailUseCase = getRideRequestDetailUseCase;
        this.giveDriverRatingUseCase = giveDriverRatingUseCase;
    }

    @Override
    public void actionGetReceipt() {
        getView().showGetReceiptLoading();
        getView().hideReceiptLayout();
        getReceiptUseCase.execute(getView().getReceiptParam(), new Subscriber<Receipt>() {
            @Override
            public void onCompleted() {
                getView().hideGetReceiptLoading();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (!isViewAttached()) return;
                getView().hideGetReceiptLoading();
                getView().showMessage(e.getMessage());
                getView().showErrorLayout();
            }

            @Override
            public void onNext(Receipt receipt) {
                if (isViewAttached()) {
                    getView().showReceiptLayout();
                    getView().renderReceipt(receipt);
                }
            }
        });
    }

    @Override
    public void actionSendRating() {
        getView().showGetReceiptLoading();
        getView().hideReceiptLayout();
        giveDriverRatingUseCase.execute(getView().getRatingParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().showMessage(s);
                }
            }
        });
    }

    @Override
    public void detachView() {
        getRideRequestDetailUseCase.unsubscribe();
        getReceiptUseCase.unsubscribe();
        super.detachView();
    }
}
