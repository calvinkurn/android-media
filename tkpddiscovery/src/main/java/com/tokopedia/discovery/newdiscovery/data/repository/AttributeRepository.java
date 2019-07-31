package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public interface AttributeRepository {

    Observable<HotlistAttributeModel> getHotlistAttribute(TKPDMapParam<String, Object> param);

    Observable<DynamicFilterModel> getDynamicFilter(TKPDMapParam<String, Object> param);

    Observable<DynamicFilterModel> getDynamicFilterV4(TKPDMapParam<String, Object> param);
}
