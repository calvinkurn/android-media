package com.tokopedia.seller.manageitem.domain.repository;



import com.tokopedia.seller.manageitem.data.cloud.model.category.CategoryRecommDataModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRecommRepository {
    Observable<CategoryRecommDataModel> fetchCategoryRecomm(String title, int row);
}
