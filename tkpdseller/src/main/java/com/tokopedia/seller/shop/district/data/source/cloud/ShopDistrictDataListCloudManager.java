package com.tokopedia.seller.shop.district.data.source.cloud;

import com.tokopedia.seller.base.data.source.cloud.DataListCloudManager;
import com.tokopedia.seller.shop.district.data.source.cloud.model.ProvincesCitiesDistrict;

import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public class ShopDistrictDataListCloudManager implements DataListCloudManager<ProvincesCitiesDistrict> {

    @Override
    public Observable<List<ProvincesCitiesDistrict>> getData() {
        return null;
    }
}
