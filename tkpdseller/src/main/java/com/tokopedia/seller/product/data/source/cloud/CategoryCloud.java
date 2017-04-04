package com.tokopedia.seller.product.data.source.cloud;

import com.tokopedia.seller.product.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.data.source.cloud.model.categorydata.CategoryServiceModel;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.shopscore.data.common.GetData;


import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */
@CategoryPickerScope
public class CategoryCloud {

    private final HadesCategoryApi api;

    @Inject
    public CategoryCloud(HadesCategoryApi api) {
        this.api = api;
    }

    public Observable<CategoryServiceModel> fetchDataFromNetwork() {
        return api.fetchCategory()
                .map(new GetData<CategoryServiceModel>());
    }
}
