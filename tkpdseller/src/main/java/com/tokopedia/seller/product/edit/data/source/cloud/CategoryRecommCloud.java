package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.seller.product.edit.data.source.cloud.api.MerlinApi;
import com.tokopedia.seller.product.edit.data.source.cloud.api.request.CategoryRecommRequest;
import com.tokopedia.seller.product.edit.data.source.cloud.api.request.Data;
import com.tokopedia.seller.product.edit.data.source.cloud.api.request.Parcel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class CategoryRecommCloud {
    private final MerlinApi api;

    @Inject
    public CategoryRecommCloud(MerlinApi api) {
        this.api = api;
    }


    public Observable<CategoryRecommDataModel> fetchData(String title,
                                                         int row) {
        List<Parcel> parcelList = new ArrayList<>();
        parcelList.add(new Parcel(new Data(title)));

        CategoryRecommRequest request = new CategoryRecommRequest(parcelList, row);

        return api.getCategoryRecomm(request);
    }
}
