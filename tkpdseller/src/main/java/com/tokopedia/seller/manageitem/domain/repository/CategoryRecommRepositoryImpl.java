package com.tokopedia.seller.manageitem.domain.repository;


import com.tokopedia.seller.manageitem.data.cloud.model.category.CategoryRecommDataModel;
import com.tokopedia.seller.manageitem.data.source.CategoryRecommDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */

public class CategoryRecommRepositoryImpl implements CategoryRecommRepository {
    private final CategoryRecommDataSource categoryRecommDataSource;

    @Inject
    public CategoryRecommRepositoryImpl(CategoryRecommDataSource categoryRecommDataSource) {
        this.categoryRecommDataSource = categoryRecommDataSource;
    }

    @Override
    public Observable<CategoryRecommDataModel> fetchCategoryRecomm(String title, int row) {
        return categoryRecommDataSource.fetchCategoryRecomm(title, row);
    }
}
