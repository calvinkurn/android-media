package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.CatalogDataSource;
import com.tokopedia.seller.product.data.source.CategoryRecommDataSource;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.domain.CatalogRepository;
import com.tokopedia.seller.product.domain.CategoryRecommRepository;

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
