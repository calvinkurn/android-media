package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.category.domain.CategoryRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryVersionDataSource categoryVersionDataSource;

    public CategoryRepositoryImpl(CategoryVersionDataSource categoryVersionDataSource) {
        this.categoryVersionDataSource = categoryVersionDataSource;
    }

    @Override
    public Observable<Boolean> checkVersion() {
        return categoryVersionDataSource.checkVersion();
    }
}
