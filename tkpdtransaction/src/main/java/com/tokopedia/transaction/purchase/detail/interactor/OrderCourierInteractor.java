package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public interface OrderCourierInteractor {

    void onGetCourierList(
            String selectedCourierId,
            TKPDMapParam<String, String> params,
            Subscriber<ListCourierViewModel> model
    );

    void confirmShipping(TKPDMapParam<String, String> params, Subscriber<String> subscriber);

    void changeCourier(Map<String, String> params, Subscriber<String> subscriber);

}
