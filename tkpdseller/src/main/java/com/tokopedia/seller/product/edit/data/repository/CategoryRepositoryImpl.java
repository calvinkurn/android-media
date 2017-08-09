package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.CategoryDataSource;
import com.tokopedia.seller.product.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.data.source.FetchCategoryDataSource;
import com.tokopedia.seller.product.edit.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.category.domain.CategoryRepository;
import com.tokopedia.seller.product.category.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.category.domain.model.CategoryLevelDomainModel;

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
    private final FetchCategoryDataSource fetchCategoryDataSource;

    @Inject
    public CategoryRepositoryImpl(CategoryVersionDataSource categoryVersionDataSource, CategoryDataSource categoryDataSource, FetchCategoryDataSource fetchCategoryDataSource) {
        this.categoryVersionDataSource = categoryVersionDataSource;
        this.categoryDataSource = categoryDataSource;
        this.fetchCategoryDataSource = fetchCategoryDataSource;
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
    public Observable<List<CategoryDomainModel>> fetchCategoryWithParent(long categoryId) {
        return fetchCategoryDataSource.fetchCategoryLevelOne(categoryId);
    }

    @Override
    public Observable<List<CategoryLevelDomainModel>> fetchCategoryFromSelected(long categoryId) {
        return fetchCategoryDataSource.fetchCategoryFromSelected(categoryId);
    }

    @Override
    public Observable<List<String>> fetchCategoryDisplay(long categoryId) {
        return fetchCategoryDataSource.fetchCategoryDisplay(categoryId);
    }

    @Override
    public Observable<Boolean> clearCache() {
        return categoryVersionDataSource.clearCache();
    }

    public Observable<String> getCategoryName(long categoryId) {
        return categoryDataSource.getCategoryName(categoryId);
    }
}

