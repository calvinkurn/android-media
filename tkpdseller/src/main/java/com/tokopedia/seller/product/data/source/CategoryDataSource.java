package com.tokopedia.seller.product.data.source;

import android.support.annotation.NonNull;

import com.tokopedia.seller.product.data.mapper.CategoryDataToDomainMapper;
import com.tokopedia.seller.product.data.mapper.CategoryServiceToDbMapper;
import com.tokopedia.seller.product.data.source.cloud.CategoryCloud;
import com.tokopedia.seller.product.data.source.db.CategoryDataManager;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

@CategoryPickerScope
public class CategoryDataSource {
    private final CategoryDataManager categoryDataManager;
    private final CategoryCloud categoryCloud;

    @Inject
    public CategoryDataSource(CategoryDataManager categoryDataManager, CategoryCloud categoryCloud) {
        this.categoryDataManager = categoryDataManager;
        this.categoryCloud = categoryCloud;
    }

    public Observable<Boolean> checkCategoryAvailable() {
        return categoryDataManager.fetchFromDatabase()
                .map(new CheckDatabaseNotNull())
                .onErrorResumeNext(fetchDataFromNetwork());
    }

    @NonNull
    private Observable<Boolean> fetchDataFromNetwork() {
        return categoryCloud.fetchDataFromNetwork()
            .map(new CategoryServiceToDbMapper())
            .map(new StoreDataToDatabase());
    }

    public Observable<List<CategoryDomainModel>> fetchCategoryLevelOne() {
        return categoryDataManager
                .fetchCategoryFromParent(CategoryDataBase.LEVEL_ONE_PARENT)
                .map(new CategoryDataToDomainMapper());
    }

    private class CheckDatabaseNotNull implements Func1<List<CategoryDataBase>, Boolean> {
        @Override
        public Boolean call(List<CategoryDataBase> categoryDataBases) {
            if(categoryDataBases == null || categoryDataBases.isEmpty()){
                throw new RuntimeException("");
            }
            return true;
        }
    }

    private class StoreDataToDatabase implements Func1<List<CategoryDataBase>, Boolean> {

        @Override
        public Boolean call(List<CategoryDataBase> categoryDataBases) {
            categoryDataManager.storeData(categoryDataBases);
            return true;
        }
    }
}
