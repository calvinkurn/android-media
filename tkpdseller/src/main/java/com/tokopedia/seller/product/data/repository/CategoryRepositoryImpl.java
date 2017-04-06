package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.CategoryDataSource;
import com.tokopedia.seller.product.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
@CategoryPickerScope
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryVersionDataSource categoryVersionDataSource;
    private final CategoryDataSource categoryDataSource;

    @Inject
    public CategoryRepositoryImpl(CategoryVersionDataSource categoryVersionDataSource, CategoryDataSource categoryDataSource) {
        this.categoryVersionDataSource = categoryVersionDataSource;
        this.categoryDataSource = categoryDataSource;
    }

    @Override
    public Observable<Boolean> checkVersion() {
        return categoryVersionDataSource.checkVersion();
    }

    @Override
    public Observable<Boolean> checkCategoryAvailable() {
        return categoryDataSource.checkCategoryAvailable();
    }

    @Override
    public Observable<List<CategoryDomainModel>> fetchCategoryLevelOne() {
        return categoryDataSource.fetchCategoryLevelOne();
    }
}
