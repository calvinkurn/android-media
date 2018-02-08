package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.seller.product.edit.data.source.CategoryRecommDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.edit.domain.CategoryRecommRepository;

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
