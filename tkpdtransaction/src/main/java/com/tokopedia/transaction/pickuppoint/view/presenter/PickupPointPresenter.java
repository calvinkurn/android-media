package com.tokopedia.transaction.pickuppoint.view.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.pickuppoint.domain.model.PickupPointResponse;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract;
import com.tokopedia.transaction.pickuppoint.view.mapper.PickupPointViewModelMapper;
import com.tokopedia.transaction.pickuppoint.view.model.StoreViewModel;

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
        getPickupPointsUseCase.unsubscribe();
    }

    @Override
    public void queryPickupPoints(String keyword, HashMap<String, String> param) {
        getView().showLoading();
        if (keyword != null && storeViewModels.size() > 0) {
            ArrayList<StoreViewModel> searchResults = new ArrayList<>();
            for (StoreViewModel storeViewModel : storeViewModels) {
                if (storeViewModel.getStore().getStoreName().toLowerCase().contains(keyword.toLowerCase()) ||
                        storeViewModel.getStore().getAddress().toLowerCase().contains(keyword.toLowerCase())) {
                    searchResults.add(storeViewModel);
                }
            }
            if (isViewAttached()) {
                getView().hideLoading();
                getView().showSearchResult(searchResults);
            }
        } else {
            getPickupPointsUseCase.execute(getParams(param), new Subscriber<PickupPointResponse>() {
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
                        if (storeViewModels.size() > 0) {
                            getView().showAllResult();
                        } else {
                            getView().showNoResult();
                        }
                    }
                }
            });
        }
    }

    @Override
    public ArrayList<StoreViewModel> getPickupPoints() {
        return storeViewModels;
    }

    private RequestParams getParams(HashMap<String, String> params) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(params);
        requestParams.putString(GetPickupPointsUseCase.PARAM_QUERY, "");

        return requestParams;
    }
}