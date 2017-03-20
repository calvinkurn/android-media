package com.tokopedia.seller.shop.setting.domain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public interface DistrictDataRepository {
    Observable<Boolean> fetchDistrictData();
}
