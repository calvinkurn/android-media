package com.tokopedia.seller.common.datasource;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.database.SellerLoginDataBase;
import com.tokopedia.seller.database.SellerLoginDataBase_Table;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 9/7/2017.
 */

public class SellerLoginDataSource {

    @Inject
    public SellerLoginDataSource() {
    }

    public Observable<Boolean> saveLoginTime(String userId){
        SellerLoginDataBase insertSellerLoginDataBase = new SellerLoginDataBase();
        insertSellerLoginDataBase.setUserId(userId);
        insertSellerLoginDataBase.setTimeStamp(System.currentTimeMillis() / 1000L);
        insertSellerLoginDataBase.save();
        return Observable.just(true);
    }
}
