package com.tokopedia.ride.bookingride.view;

import android.content.Intent;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/22/17.
 */

public class ConfirmBookingPresenter extends BaseDaggerPresenter<ConfirmBookingContract.View>
        implements ConfirmBookingContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private NetworkInteractor networkInteractor;

    public ConfirmBookingPresenter(GetFareEstimateUseCase getFareEstimateUseCase) {
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.networkInteractor = new NetworkInteractorImpl();
    }

    @Override
    public void initialize() {
        actionCheckBalance();
    }

    @Override
    public void actionCheckBalance() {
        getView().showProgress();
        getView().hideConfirmButton();
        getView().hideTopupTokoCashButton();
        networkInteractor.getTokoCash(getView().getActivity(), new NetworkInteractor.TopCashListener() {
            @Override
            public void onSuccess(TopCashItem topCashItem) {
                //get balance int value
                String balanceStr = topCashItem.getData().getBalance();
                int balance = 1000000;
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    balance = CurrencyFormatHelper.convertRupiahToInt(topCashItem.getData().getBalance());
                }

                getView().hideProgress();

                if (balance < getView().getFarePrice()) {
                    getView().showTopupTokoCashButton();
                    getView().hideConfirmButton();
                    getView().setBalanceText(topCashItem.getData().getBalance());
                } else {
                    getView().hideTopupTokoCashButton();
                    getView().showConfirmButton();
                }
            }

            @Override
            public void onError(String message) {
                getView().hideProgress();
                getView().showMessage(message);
                getView().showTopupTokoCashButton();
                getView().hideConfirmButton();
            }

            @Override
            public void onTokenExpire() {
                Intent intent = new Intent();
                intent.setAction("com.tokopedia.tkpd.FORCE_LOGOUT");
                MainApplication.getAppContext().sendBroadcast(intent);
            }
        });
    }

    @Override
    public void actionChangeSeatCount() {
        RequestParams requestParams = getView().getParam();
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showErrorChangeSeat(e.getMessage());
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                getView().renderFareEstimate(fareEstimate.getFare().getFareId(), fareEstimate.getFare().getDisplay(), fareEstimate.getFare().getValue());
                actionCheckBalance();
            }
        });
    }
}
