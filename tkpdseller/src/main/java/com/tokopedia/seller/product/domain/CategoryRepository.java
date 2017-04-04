package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.CategoryGroupDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRepository {
    Observable<Boolean> checkVersion();

    Observable<CategoryGroupDomainModel> fetchCategory();
}
