package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.seller.product.edit.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.CategoryVersionServiceModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class CategoryVersionCloud {
    private final HadesCategoryApi api;

    @Inject
    public CategoryVersionCloud(HadesCategoryApi api) {
        this.api = api;
    }

    public Observable<CategoryVersionServiceModel> checkVersion() {
        return api
                .checkVersion()
                .map(new GetData<CategoryVersionServiceModel>());
    }
}
