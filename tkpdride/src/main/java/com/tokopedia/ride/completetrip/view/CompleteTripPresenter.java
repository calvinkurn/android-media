package com.tokopedia.ride.completetrip.view;

import android.os.Build;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetPayPendingDataUseCase;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.PayPending;
import com.tokopedia.ride.common.ride.domain.model.Receipt;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.completetrip.domain.SendTipUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/31/17.
 */

public class CompleteTripPresenter extends BaseDaggerPresenter<CompleteTripContract.View>
        implements CompleteTripContract.Presenter {
    private static final String UBER_SHOURTCUT_ALERT_SHOWN_KEY = "UBER_SHOURTCUT_ALERT_SHOWN_KEY";
    private GetReceiptUseCase getReceiptUseCase;
    private GetRideRequestDetailUseCase getRideRequestDetailUseCase;
    private GiveDriverRatingUseCase giveDriverRatingUseCase;
    private GetSingleRideHistoryUseCase getSingleRideHistoryUseCase;
    private GetPayPendingDataUseCase getPayPendingDataUseCase;
    private SendTipUseCase sendTipUseCase;

    @Inject
    public CompleteTripPresenter(GetReceiptUseCase getReceiptUseCase,
                                 GetRideRequestDetailUseCase getRideRequestDetailUseCase,
                                 GiveDriverRatingUseCase giveDriverRatingUseCase,
                                 GetSingleRideHistoryUseCase getSingleRideHistoryUseCase,
                                 SendTipUseCase sendTipUseCase,
                                 GetPayPendingDataUseCase getPayPendingDataUseCase) {
        this.getReceiptUseCase = getReceiptUseCase;
        this.getRideRequestDetailUseCase = getRideRequestDetailUseCase;
        this.giveDriverRatingUseCase = giveDriverRatingUseCase;
        this.getSingleRideHistoryUseCase = getSingleRideHistoryUseCase;
        this.sendTipUseCase = sendTipUseCase;
        this.getPayPendingDataUseCase = getPayPendingDataUseCase;
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
                    showPopupToAddShortcutForFirstTime();

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
    public void handleRatingStarClick(float rating) {
        if (rating > 0) {
            getView().enableRatingSubmitButton();
            if (rating >= 4 && getView().getFormmattedTipList() != null && getView().getFormmattedTipList().size() > 0) {
                getView().showTipLayout();
            } else {
                getView().hideTipLayout();
            }

        } else {
            getView().disableRatingSubmitButton();
        }
    }

    @Override
    public void actionSubmitRatingAndDriverTip() {
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
                    getView().showReceiptLayout();

                    String message = e.getMessage();
                    if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    }

                    getView().showErrorInRating(message);
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    if (getView().getTipAmount() > 0) {
                        sendTip(getView().getTipParam());
                    } else {
                        getView().showRatingResultLayout(Integer.parseInt(getView().getRateStars()));
                        getView().hideGetReceiptLoading();
                        getView().showReceiptLayout();
                        getView().hideRatingLayout();
                        getView().closePage();
                    }
                }
            }
        });
    }

    @Override
    public void sendTip(RequestParams tipParams) {
        sendTipUseCase.execute(tipParams, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideGetReceiptLoading();
                    getView().showReceiptLayout();

                    String message = e.getMessage();
                    if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    }

                    getView().showErrorInDriverTipping(message);
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
    public void payPendingFare() {
        getView().showProgressbar();
        getPayPendingDataUseCase.execute(RequestParams.EMPTY, new Subscriber<PayPending>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressbar();
                e.printStackTrace();
            }

            @Override
            public void onNext(PayPending payPending) {
                getView().hideProgressbar();
                getView().openScroogePage(payPending.getUrl(), payPending.getPostData());
            }
        });
    }

    @Override
    public void showPopupToAddShortcutForFirstTime() {
        if (!isViewAttached()) {
            return;
        }

        //do not show popup below M
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        try {
            GlobalCacheManager cacheManager = new GlobalCacheManager();
            String cache = cacheManager.getValueString(UBER_SHOURTCUT_ALERT_SHOWN_KEY);
            if (cache == null || !cache.equalsIgnoreCase("1")) {
                getView().showAddShortcutDialog();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void setShortcutDialogIsShowninCache() {
        try {
            GlobalCacheManager cacheManager = new GlobalCacheManager();
            cacheManager.setKey(UBER_SHOURTCUT_ALERT_SHOWN_KEY);
            cacheManager.setValue("1");
            cacheManager.store();
        } catch (Exception ex) {

        }
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
