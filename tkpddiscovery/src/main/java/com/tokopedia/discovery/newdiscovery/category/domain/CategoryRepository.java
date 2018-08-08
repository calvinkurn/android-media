package com.tokopedia.discovery.newdiscovery.category.domain;


import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import rx.Observable;

/**
 * @author by alifa on 10/30/17.
 */

public interface CategoryRepository {

    Observable<CategoryHeaderModel> getCategoryHeader(String categoryId);
}
