package com.tokopedia.ride.completetrip.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/31/17.
 */

public class CompleteTripPresenter extends BaseDaggerPresenter<CompleteTripContract.View>
        implements CompleteTripContract.Presenter {
    private GetReceiptUseCase getReceiptUseCase;
    private GetRideRequestDetailUseCase getRideRequestDetailUseCase;
    private GiveDriverRatingUseCase giveDriverRatingUseCase;
    private GetSingleRideHistoryUseCase getSingleRideHistoryUseCase;

    @Inject
    public CompleteTripPresenter(GetReceiptUseCase getReceiptUseCase,
                                 GetRideRequestDetailUseCase getRideRequestDetailUseCase,
                                 GiveDriverRatingUseCase giveDriverRatingUseCase,
                                 GetSingleRideHistoryUseCase getSingleRideHistoryUseCase) {
        this.getReceiptUseCase = getReceiptUseCase;
        this.getRideRequestDetailUseCase = getRideRequestDetailUseCase;
        this.giveDriverRatingUseCase = giveDriverRatingUseCase;
        this.getSingleRideHistoryUseCase = getSingleRideHistoryUseCase;
    }

    @Override
    public void actionGetReceipt() {
        getView().clearRideNotificationIfExists();
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
                    boolean isPendingPaymentExists = (receipt != null && receipt.getPendingPayment() != null &&
                            receipt.getPendingPayment().getPendingAmount() != null &&
                            receipt.getPendingPayment().getPendingAmount().length() > 0);

                    getView().showReceiptLayout();
                    getView().renderReceipt(receipt, isPendingPaymentExists);

                    if (getView().isCameFromPushNotif() && !isPendingPaymentExists) {
                        getView().hideRatingLayout();
                        actionCheckIfAlreadySendRating();
                    } else if (isPendingPaymentExists) {
                        getView().hideRatingLayout();
                    } else {
                        getView().showRatingLayout();
                    }
                }
            }
        });
    }

    private void actionCheckIfAlreadySendRating() {
        getSingleRideHistoryUseCase.execute(getView().getRideHistoryParam(), new Subscriber<RideHistory>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RideHistory rideHistory) {
                if (isViewAttached()) {
                    if (rideHistory.getStatus().equalsIgnoreCase(RideStatus.COMPLETED) &&
                            rideHistory.getRating() != null &&
                            !rideHistory.getRating().getStar().equalsIgnoreCase("0")) {
                        getView().showRatingResultLayout(Integer.parseInt(rideHistory.getRating().getStar()));
                    } else {
                        getView().showRatingLayout();
                    }
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
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideGetReceiptLoading();
                    getView().showRatingErrorLayout();
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().showRatingResultLayout(Integer.parseInt(getView().getRateStars()));
                    getView().hideGetReceiptLoading();
                    getView().showReceiptLayout();
                    getView().hideRatingLayout();
                    getView().closePage();
                }
            }
        });
    }

    @Override
    public void detachView() {
        getRideRequestDetailUseCase.unsubscribe();
        getReceiptUseCase.unsubscribe();
        getSingleRideHistoryUseCase.unsubscribe();
        giveDriverRatingUseCase.unsubscribe();
        super.detachView();
    }
}
