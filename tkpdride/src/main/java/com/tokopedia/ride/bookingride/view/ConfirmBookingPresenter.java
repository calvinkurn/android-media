package com.tokopedia.ride.bookingride.view;

import android.content.Intent;
import android.net.Uri;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
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
        getView().hideNotActivatedTokoCashLayout();
        networkInteractor.getTokoCash(getView().getActivity(), new NetworkInteractor.TopCashListener() {
            @Override
            public void onSuccess(TopCashItem topCashItem) {
                if (topCashItem.getData().getLink() != 0) {
                    //get balance int value
                    String balanceStr = topCashItem.getData().getBalance();
                    int balance = 0;
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
                    getView().hideNotActivatedTokoCashLayout();
                } else {
                    // not activated tokocash
                    getView().hideProgress();
                    getView().hideTopupTokoCashButton();
                    getView().hideConfirmButton();

                    String seamlessURL = URLGenerator.generateURLSessionLogin(
                            (Uri.encode(topCashItem.getData().getAction().getRedirectUrl())),
                            getView().getActivity()
                    );
                    getView().showNotActivatedTokoCashLayout(seamlessURL);
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
