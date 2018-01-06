package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.activity.ConfirmShippingView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderCourierInteractor;
import com.tokopedia.transaction.purchase.detail.interactor.OrderCourierInteractorImpl;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import rx.Subscriber;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class OrderCourierPresenterImpl implements OrderCourierPresenter {

    private OrderCourierInteractor interactor;

    private ConfirmShippingView view;

    public OrderCourierPresenterImpl(OrderCourierInteractorImpl interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onGetCourierList(Context context) {
        interactor.onGetCourierList(
                AuthUtil.generateParamsNetwork(context, new TKPDMapParam<String, String>()),
                new Subscriber<ListCourierViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ListCourierViewModel courierViewModel) {
                        view
                    }
                });
    }
}
