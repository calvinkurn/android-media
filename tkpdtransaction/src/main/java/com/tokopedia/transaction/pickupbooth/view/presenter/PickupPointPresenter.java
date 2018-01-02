package com.tokopedia.transaction.pickupbooth.view.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.pickupbooth.domain.model.PickupPointResponse;
import com.tokopedia.transaction.pickupbooth.domain.model.Store;
import com.tokopedia.transaction.pickupbooth.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;
import com.tokopedia.transaction.pickupbooth.view.mapper.PickupPointViewModelMapper;
import com.tokopedia.transaction.pickupbooth.view.model.StoreViewModel;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointPresenter extends BaseDaggerPresenter<PickupPointContract.View>
        implements PickupPointContract.Presenter {

    private GetPickupPointsUseCase getPickupPointsUseCase;
    private ArrayList<StoreViewModel> storeViewModels = new ArrayList<>();
    private PickupPointViewModelMapper pickupPointViewModelMapper;

    @Inject
    public PickupPointPresenter(GetPickupPointsUseCase getPickupPointsUseCase,
                                PickupPointViewModelMapper pickupPointViewModelMapper) {
        this.getPickupPointsUseCase = getPickupPointsUseCase;
        this.pickupPointViewModelMapper = pickupPointViewModelMapper;
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
    public void queryPickupPoints(String keyword, HashMap<String, String> param) {
        getView().showLoading();
        getPickupPointsUseCase.execute(getParams(keyword, param), new Subscriber<PickupPointResponse>() {
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
                    getView().hideLoading();
                    for (Store store : pickupPointResponse.getData().getStores()) {
                        storeViewModels.add(pickupPointViewModelMapper.transform(store));
                    }
                    getView().showResult();
                }
            }
        });
    }

    @Override
    public ArrayList<StoreViewModel> getPickupPoints() {
        return storeViewModels;
    }

    private RequestParams getParams(String keyword, HashMap<String, String> params) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(params);
        requestParams.putString(GetPickupPointsUseCase.PARAM_QUERY, keyword);

        Log.e("PickupPointPresParams", "This");
        for (Map.Entry<String, String> entry : requestParams.getParamsAllValueInString().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.e(key, value);
        }

        return requestParams;
    }
}
