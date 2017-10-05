package com.tokopedia.gm.cashback.domain;

import com.tokopedia.seller.common.cashback.DataCashbackModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public interface GMCashbackRepository {
    Observable<Boolean> setCashback(String product_id, String cashback);

    Observable<List<DataCashbackModel>> getCashbackList(String productIds, String shopId);
}
