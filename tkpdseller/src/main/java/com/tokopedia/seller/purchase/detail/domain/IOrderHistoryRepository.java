package com.tokopedia.seller.purchase.detail.domain;

import com.tokopedia.seller.purchase.detail.model.history.viewmodel.OrderHistoryData;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface IOrderHistoryRepository {

    Observable<OrderHistoryData> requestOrderHistoryData(HashMap<String, Object> params);

}
