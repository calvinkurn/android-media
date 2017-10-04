package com.tokopedia.gm.cashback.data.repository;

import com.tokopedia.gm.cashback.data.source.GMCashbackDataSource;
import com.tokopedia.gm.cashback.domain.GMCashbackRepository;
import com.tokopedia.seller.common.cashback.DataCashbackModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class GmCashbackRepositoryImpl implements GMCashbackRepository {
    private final GMCashbackDataSource gmCashbackDataSource;

    public GmCashbackRepositoryImpl(GMCashbackDataSource gmCashbackDataSource) {
        this.gmCashbackDataSource = gmCashbackDataSource;
    }

    @Override
    public Observable<Boolean> setCashback(String product_id, String cashback) {
        return gmCashbackDataSource.setCashback(product_id, cashback);
    }

    @Override
    public Observable<List<DataCashbackModel>> getCashbackList(String productIds, String shopId) {
        return gmCashbackDataSource.getCashbackList(productIds, shopId);
    }
}
