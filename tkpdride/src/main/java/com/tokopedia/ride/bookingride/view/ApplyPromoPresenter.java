package com.tokopedia.ride.bookingride.view;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetPromoUseCase;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/25/17.
 */

public class ApplyPromoPresenter extends BaseDaggerPresenter<ApplyPromoContract.View>
        implements ApplyPromoContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private GetPromoUseCase promoUseCase;

    @Inject
    public ApplyPromoPresenter(GetFareEstimateUseCase applyPromoUseCase, GetPromoUseCase promoUseCase) {
        this.getFareEstimateUseCase = applyPromoUseCase;
        this.promoUseCase = promoUseCase;
    }

    @Override
    public void actionApplyPromo() {
        getView().disableApplyButton();
        getView().hideErrorPromoMessage();
        if (TextUtils.isEmpty(getView().getPromo()) || getView().getPromo().length() == 0) {
            return;
        } else {
            getView().clearEmptyPromoError();
        }
        getView().showApplyPromoLoading();
        getFareEstimateUseCase.execute(getView().getParams(), new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                getView().showApplyPromoLayout();
                getView().hideApplyPromoLoading();
                getView().enableApplyButton();

                String message = e.getMessage();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                } else if (e instanceof SocketTimeoutException) {
                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                } else if (e instanceof UnProcessableHttpException) {
                    message = e.getMessage();
                }

                getView().onFailedApplyPromo(message);
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached()) {
                    getView().enableApplyButton();
                    getView().showApplyPromoLayout();
                    if (fareEstimate.isSuccess()) {
                        getView().hideApplyPromoLoading();
                        getView().onSuccessApplyPromo(fareEstimate);
                    } else {
                        getView().hideApplyPromoLoading();
                        getView().onFailedApplyPromo(fareEstimate.getAttributes().getDetail());
                    }
                }
            }
        });
    }

    @Override
    public void getOnGoingPromo() {
        getView().showPromoLoading();
        promoUseCase.execute(getView().getPromoParams(), new Subscriber<List<Promo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (isViewAttached()) {
                    getView().hidePromoLoading();
                    getView().showApplyPromoLayout();
                }
            }

            @Override
            public void onNext(List<Promo> promos) {
                if (isViewAttached()) {
                    getView().hidePromoLoading();
                    getView().showApplyPromoLayout();
                    if (promos.size() > 0) {
                        getView().renderPromoList(promos);
                    } else {
                        getView().renderEmptyOnGoingPromo();
                    }
                }
            }
        });
    }
}
