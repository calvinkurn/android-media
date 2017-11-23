package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.DynamicAttributeMapper;
import com.tokopedia.discovery.newdiscovery.hotlist.data.mapper.HotlistAttributeMapper;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/6/17.
 */

public class AttributeDataSource {

    private final BrowseApi attributeApi;
    private final HotlistAttributeMapper hotlistAttributeMapper;
    private final DynamicAttributeMapper dynamicAttributeMapper;

    public AttributeDataSource(BrowseApi attributeApi,
                               HotlistAttributeMapper hotlistAttributeMapper,
                               DynamicAttributeMapper dynamicAttributeMapper) {
        this.attributeApi = attributeApi;
        this.hotlistAttributeMapper = hotlistAttributeMapper;
        this.dynamicAttributeMapper = dynamicAttributeMapper;
    }

    public Observable<HotlistAttributeModel> getHotlistAttribute(TKPDMapParam<String, Object> param) {
        return attributeApi.getAttribute(param).map(hotlistAttributeMapper);
    }


    public Observable<DynamicFilterModel> getDynamicAttribute(TKPDMapParam<String, Object> param) {
        return attributeApi.getDynamicAttribute(param).map(dynamicAttributeMapper);
    }
}
