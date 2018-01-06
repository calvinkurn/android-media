package com.tokopedia.transaction.purchase.detail.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public interface IOrderCourierRepository {

    Observable<ListCourierViewModel> onOrderCourierRepository(TKPDMapParam<String, String> params);

}
