package com.tokopedia.gm.cashback.data.source;

import com.tokopedia.gm.cashback.data.model.RequestCashbackModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class GMCashbackDataSource {
    public static final String WORDING_SUCCESS_CASHBACK = "Set Cashback Success";
    private final GMCashbackDataSourceCloud gmCashbackDataSourceCloud;

    @Inject
    public GMCashbackDataSource(GMCashbackDataSourceCloud gmCashbackDataSourceCloud) {
        this.gmCashbackDataSourceCloud = gmCashbackDataSourceCloud;
    }

    public Observable<Boolean> setCashback(String product_id, String cashback) {
        return gmCashbackDataSourceCloud.setCashback(new RequestCashbackModel(product_id, cashback))
                .map(new SimpleDataResponseMapper<String>())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        if(s != null && s.equals(WORDING_SUCCESS_CASHBACK)){
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
    }
}
