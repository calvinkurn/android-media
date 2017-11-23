package com.tokopedia.discovery.newdiscovery.category.data.source;

import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.newdiscovery.category.data.mapper.CategoryHeaderMapper;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import rx.Observable;

import static com.tokopedia.core.network.apiservices.hades.apis.HadesApi.ANDROID_DEVICE;

/**
 * @author by alifa on 10/30/17.
 */

public class CategoryDataSource {

    private final HadesApi hadesApi;
    private final CategoryHeaderMapper mapper;

    public CategoryDataSource(HadesApi hadesApi, CategoryHeaderMapper mapper) {
        this.hadesApi = hadesApi;
        this.mapper = mapper;
    }

    public Observable<CategoryHeaderModel> getHeader(String categoryId) {
        return hadesApi.getCategories(ANDROID_DEVICE,categoryId).map(mapper);
    }


}
