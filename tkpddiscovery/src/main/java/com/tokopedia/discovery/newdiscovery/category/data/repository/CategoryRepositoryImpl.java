package com.tokopedia.discovery.newdiscovery.category.data.repository;

import com.tokopedia.discovery.newdiscovery.category.data.source.CategoryDataSource;
import com.tokopedia.discovery.newdiscovery.category.domain.CategoryRepository;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import rx.Observable;

/**
 * @author by alifa on 10/30/17.
 */

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryDataSource categoryDataSource;

    public CategoryRepositoryImpl(CategoryDataSource categoryDataSource) {
        this.categoryDataSource = categoryDataSource;
    }

    @Override
    public Observable<CategoryHeaderModel> getCategoryHeader(String categoryId) {
        return categoryDataSource.getHeader(categoryId);
    }
}
