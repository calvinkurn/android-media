package com.tokopedia.transaction.pickupbooth.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.pickupbooth.domain.model.PickupPointResponse;
import com.tokopedia.transaction.pickupbooth.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointPresenter extends BaseDaggerPresenter<PickupPointContract.View>
        implements PickupPointContract.Presenter {

    private GetPickupPointsUseCase getPickupPointsUseCase;
    private String token;
    private String ut;

    @Inject
    public PickupPointPresenter(GetPickupPointsUseCase getPickupPointsUseCase) {
        this.getPickupPointsUseCase = getPickupPointsUseCase;
    }

    @Override
    public void attachView(PickupPointContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void queryPickupPoints(String keyword) {
        getView().showLoading();
        getPickupPointsUseCase.execute(getParams(), new Subscriber<PickupPointResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                    String message;
                    if (e instanceof UnknownHostException || e instanceof ConnectException ||
                            e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.msg_no_connection);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.msg_no_connection) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.default_request_error_unknown);
                    }
                    getView().showNoConnection(message);
                }
            }

            @Override
            public void onNext(PickupPointResponse pickupPointResponse) {
                if (isViewAttached()) {
                    getView().
                }
            }
        });
    }

    private RequestParams getParams() {
        RequestParams params = RequestParams.create();
//        params.putString(GetPickupPointsUseCase.PARAM_PAGE, GetPickupPointsUseCase.DEFAULT_PAGE);
//        params.putString(GetPickupPointsUseCase.PARAM_TOKEN,
//                token.getDistrictRecommendation());
//        params.putString(GetPickupPointsUseCase.PARAM_UT,
//                String.valueOf(token.getUnixTime()));
//        params.putString(GetPickupPointsUseCase.PARAM_QUERY, query);
        return params;
    }
}
