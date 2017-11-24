package com.tokopedia.seller.shop.district.data.source.database;

import com.tokopedia.seller.base.data.source.database.DataListDBManager;
import com.tokopedia.seller.shop.district.data.source.cloud.model.ProvincesCitiesDistrict;

import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public class ShopDistrictDataListDBManager implements DataListDBManager<ProvincesCitiesDistrict>{

    @Override
    public Observable<Boolean> deleteAll() {
        return null;
    }

    @Override
    public Observable<Boolean> insertAll(List<ProvincesCitiesDistrict> list) {
        return null;
    }
}
