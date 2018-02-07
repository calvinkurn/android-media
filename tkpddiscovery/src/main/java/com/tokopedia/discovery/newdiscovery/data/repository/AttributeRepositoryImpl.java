package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.source.AttributeDataSource;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class AttributeRepositoryImpl implements AttributeRepository {

    private final AttributeDataSource attributeDataSource;

    public AttributeRepositoryImpl(AttributeDataSource attributeDataSource) {
        this.attributeDataSource = attributeDataSource;
    }

    @Override
    public Observable<HotlistAttributeModel> getHotlistAttribute(TKPDMapParam<String, Object> param) {
        return attributeDataSource.getHotlistAttribute(param);
    }

    @Override
    public Observable<DynamicFilterModel> getDynamicFilter(TKPDMapParam<String, Object> param) {
        return attributeDataSource.getDynamicAttribute(param);
    }
}
