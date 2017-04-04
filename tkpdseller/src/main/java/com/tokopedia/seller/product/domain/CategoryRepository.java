package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.CategoryGroupDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRepository {
    Observable<Boolean> checkVersion();

    Observable<List<CategoryGroupDomainModel>> fetchCategory();
}
